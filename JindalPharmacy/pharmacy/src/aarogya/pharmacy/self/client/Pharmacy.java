package aarogya.pharmacy.self.client;

import aarogya.pharmacy.additionalPlaceOfBusiness.client.AdditionalPlaceOfBusiness;
import aarogya.pharmacy.changePaymentSource.client.ChangePaymentSource;
import aarogya.pharmacy.customerMaster.client.CustomerMaster;
import aarogya.pharmacy.d365StockRecive.client.GRN;
import aarogya.pharmacy.egstParam.client.EgstParam;
import aarogya.pharmacy.enquiry.client.Enquiry;
import aarogya.pharmacy.enquiryApproval.client.EnquiryApproval;
import aarogya.pharmacy.environmentSettingSite.client.EnvironmentSettingSite;
import aarogya.pharmacy.expiryModification.client.ExpiryModification;
import aarogya.pharmacy.pharmacyReceipt.client.PharmacyReceipt;
import aarogya.pharmacy.grnApproval.client.GrnApproval;
import aarogya.pharmacy.indentApproval.client.IndentApproval;
import aarogya.pharmacy.indentClose.client.IndentClose;
import aarogya.pharmacy.indent.client.Indent;
import aarogya.pharmacy.ledgerMaster.client.LedgerMaster;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.pharmacy.medicineCategoryMaster.client.MedicineCategoryMaster;
import aarogya.pharmacy.materialCategoryMast.client.MaterialCategoryMast;
import aarogya.pharmacy.medicineCompositionMaster.client.MedicineCompositionMaster;
import aarogya.pharmacy.medicineDepartmentInfo.client.MedicineDepartmentInfo;
import aarogya.pharmacy.medicineGroupMaster.client.MedicineGroupMaster;
import aarogya.pharmacy.medicineIssue.client.MedicineIssue;
import aarogya.pharmacy.medicineMakeMaster.client.MedicineMakeMaster;
import aarogya.pharmacy.medicineMaster.client.MedicineMaster;
import aarogya.pharmacy.medicineModification.client.MedicineModification;
import aarogya.pharmacy.medicineOpening.client.MedicineOpening;
import aarogya.pharmacy.medicineReceive.client.MedicineReceive;
import aarogya.pharmacy.medicineRequirement.client.MedicineRequirement;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineTemplateMaster.client.MedicineTemplateMaster;
import aarogya.pharmacy.operationMediMaster.client.OperationMediMaster;
import aarogya.pharmacy.reports.client.RptIndentEntryRegister;
import aarogya.pharmacy.requisition.client.Requisition;
import aarogya.pharmacy.medicineTransfer.client.MedicineTransfer;
import aarogya.pharmacy.medicineTransferWholeSale.client.MedicineTransferWholeSale;
import aarogya.pharmacy.medicineInTransit.client.MedicineInTransit;
import aarogya.pharmacy.medicineRequisitionDisplay.client.MedicineRequisitionDisplay;
import aarogya.pharmacy.medicineIndentDisplay.client.MedicineIndentnDisplay;
import aarogya.pharmacy.medicineTransferApproval.client.MedicineTransferApproval;
import aarogya.pharmacy.procurementPlannerDisplay.client.ProcurementPlannerDisplay;
import aarogya.pharmacy.organizationBillingOnRateMaster.client.OrganizationBillingOnRateMaster;
import aarogya.pharmacy.pendingPODisplay.client.PendingPO_Display;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.client.PharmacyFinalDiscountEntry;
import aarogya.pharmacy.pharmacyRefund.client.PharmacyRefund;
import aarogya.pharmacy.physicalStock.client.PhysicalStock;
import aarogya.pharmacy.physicalStockClearance.client.PhysicalStockClearance;
import aarogya.pharmacy.physicalStockPosting.client.PhysicalStockPosting;
import aarogya.pharmacy.profitSlab.client.ProfitSlab;
import aarogya.pharmacy.proformaPurchaseReturn.client.ProformaPurchaseReturn;
import aarogya.pharmacy.purchaseBill.client.PurchaseBill;
import aarogya.pharmacy.purchaseBillApproval.client.PurchaseBillApproval;
import aarogya.pharmacy.purchaseBillDirect.client.PurchaseBillDirect;
import aarogya.pharmacy.saleBill.client.SaleBill;
import aarogya.pharmacy.saleBillWholeSale.client.SaleBillWholeSale;
import aarogya.pharmacy.purchaseOrder.client.PurchaseOrder;
import aarogya.pharmacy.purchaseOrderApproval.client.PurchaseOrderApproval;
import aarogya.pharmacy.purchaseOrderClose.client.PurchaseOrderClose;
import aarogya.pharmacy.purchaseReturn.client.PurchaseReturn;
import aarogya.pharmacy.purchaseReturnApproval.client.PurchaseReturnApproval;
import aarogya.pharmacy.quotation.client.Quotation;
import aarogya.pharmacy.reports.client.*;
import aarogya.pharmacy.requisitionApproval.client.RequisitionApproval;
import aarogya.pharmacy.saleBillReturn.client.SaleBillReturn;
import aarogya.pharmacy.saleBillReturnProvisional.client.SaleBillReturnProvisional;
import aarogya.pharmacy.stockBatch.client.StockBatch;
import aarogya.pharmacy.stockLedger.client.StockLedger;
import aarogya.pharmacy.reports.client.RptPharmacyStkReport;
import aarogya.pharmacy.requisitionClose.client.RequisitionClose;
import aarogya.pharmacy.stockSummary.client.StockSummary;
import aarogya.pharmacy.vehicleTypeMaster.client.VehicleTypeMaster;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
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
import aarogya.pharmacy.vendorMaster.client.VendorMaster;
import aarogya.pharmacy.wholeSaleBillReturn.client.WholeSaleBillReturn;
import aarogya.pharmacy.wholeSaleBillSettelement.client.WholeSaleBillSettelement;
import aarogya.pharmacy.wholeSalePaymentRecipt.client.WholeSalePaymentRecipt;
import aarogya.pharmacy.wholeSaleReports.client.BillWiseOutstandingDebtors;
import aarogya.pharmacy.wholeSaleReports.client.CustomerCreditLimitBalanceRegister;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleBillRegister;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleBillReturnRegister;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleStockLedger;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleStockSummary;
import aarogya.pharmacy.wholeSaleSettings.client.WholeSaleSetting;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import dataman.dmToolkit.singleLineMaster.client.SingleLineMaster;
import java.util.HashMap;
import java.util.List;

public class Pharmacy implements IsWidget {

    private pmPharmacy pmMenu = new pmPharmacy();
    private DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private DmMenuBar menu;

    public Pharmacy(final SetWidgetInvoker swiMain) {
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

            if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_COMPOSITION_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineCompositionMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFIT_SLAB.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ProfitSlab(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_MAKE_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineMakeMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.RESTRICTED_GROUP_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Restricted Group Master", "restrictedGrpMast", "Restricted Group", "Code", "Name", 50, false, false, SQLDataType.INT, menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.UNIT_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        swiMain.setWidget(new pmInventory().moduleName, new pmInventory().UNIT_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
                        swiMain.setWidget(new pmInventory().moduleName, new pmInventory().IMPURITY_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
                        swiMain.setWidget(new pmInventory().moduleName, menuText, paramHashMapTemp, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CUSTOMER_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new CustomerMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MANUFACTURER_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        swiMain.setWidget(new pmInventory().moduleName, new pmInventory().MANUFACTURER_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
                        swiMain.setWidget(new pmInventory().moduleName, new pmInventory().DIVISION_MASTER.caption, paramHashMap, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_CATEGORY_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineCategoryMaster(menuPermission, swiMain);
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
                        new MaterialCategoryMast(menuPermission, paramHashMapTemp, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_MODEL_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Medicine Model Master", "itemModelMast", "Model Name", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_STRENGTH_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Medicine Strength Master", "itemStrengthMast", "Strength Name", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_GROUP_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineGroupMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_TEMPLATE_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineTemplateMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OPERATION_MEDICINE_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new OperationMediMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_FINAL_DISCOUNT_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PharmacyFinalDiscountEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEDGER_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new LedgerMaster(menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_MASTER.caption)) {
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
                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                            paramHashMapTemp.put("flagOpenAsPopup", false);
                        }
                        new MedicineMaster(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_OPENING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineOpening(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_RECEIVE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineReceive(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_ISSUE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineIssue(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_TRANSFER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineTransfer(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_TRANSFER_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineTransferApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_TRANSFER_WHOLESALE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineTransferWholeSale(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_IN_TRANSIT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineInTransit(menuPermission, swiMain);
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

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EXPIRY_MODIFICATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ExpiryModification(menuPermission, swiMain);
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

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ESTIMATE.caption)) {
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
                            paramHashMapTemp.put("widgetName", new pmPharmacy().ESTIMATE.caption);
                        }

                        if (!paramHashMapTemp.containsKey("vCategory")) {
                            paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().PHARMACY_ESTIMATE);
                        }

                        new SaleBill(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ESTIMATE_BILL.caption)) {
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
                            paramHashMapTemp.put("widgetName", new pmPharmacy().ESTIMATE_BILL.caption);
                        }

                        if (!paramHashMapTemp.containsKey("vCategory")) {
                            paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().PHARMACY_ESTIMATE_BILL);
                        }

                        new SaleBill(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_BILL.caption)) {
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
                            paramHashMapTemp.put("widgetName", new pmPharmacy().SALE_BILL.caption);
                        }

                        if (!paramHashMapTemp.containsKey("vCategory")) {
                            paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().PHARMACY_BILL_CASH);
                        }

                        new SaleBill(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_BILL_RETURN.caption)) {
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
                            paramHashMapTemp.put("widgetName", new pmPharmacy().SALE_BILL_RETURN.caption);
                        }

                        if (!paramHashMapTemp.containsKey("vCategory")) {
                            paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().PHARMACY_BILL_RETURN + "','" + new ProjectVoucherCategory().PHARMACY_CASH_SALE_RETURN);
                        }

                        new SaleBillReturn(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_BILL_WHOLE_SALE.caption)) {
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
                            paramHashMapTemp.put("widgetName", new pmPharmacy().SALE_BILL_WHOLE_SALE.caption);
                        }

                        if (!paramHashMapTemp.containsKey("vCategory")) {
                            paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CASH);
                            paramHashMapTemp.put("vCategory1", new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CREDIT);
                        }

                        new SaleBillWholeSale(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHARMACY_RECEIPT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PharmacyReceipt(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHARMACY_REFUND.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PharmacyRefund(menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFITABILITY_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptProfitabilityReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFITABILITY_REPORT_M.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptProfitabilityReportM(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INDENT_REGISTER.caption)) {
                //==================== Reports =================================
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndentEntryRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENQUIRY_REGISTER.caption)) {
                //==================== Reports =================================
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SUB_CATEGORY_MASTER.caption)) {
                new SingleLineMaster(pmMenu.SUB_CATEGORY_MASTER.caption, "medicineSubCategoryMast", "Sub Category", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } //======================= Reports =====================================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.PO_RELEASE_AMOUNT_WITH_CD.caption)) {
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.QUOTATION_REGISTER.caption)) {
                //==================== Reports =================================
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_BILL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseBill(menuPermission, swiMain);
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
                        new PurchaseBillDirect(menuPermission, swiMain);
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
                        new GRN(menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ESTIMATE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptEstimateRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_BILL_RETURN_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptSaleBillReturnRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_BILL_COLLECTION_REGISTER.caption)) {
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

                        new RptSaleBillCollection(swiMain, paramHashMapTemp);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_BILL_REGISTER.caption)) {
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

                        if (!paramHashMapTemp.containsKey("category")) {
                            paramHashMapTemp.put("category", new ProjectVoucherCategory().PHARMACY_BILL + "','" + new ProjectVoucherCategory().PHARMACY_BILL_CASH);
                        }

                        new RptSaleBillRegister(swiMain, paramHashMapTemp);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ESTIMATE_BILL_REGISTER.caption)) {
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

                        if (!paramHashMapTemp.containsKey("category")) {
                            paramHashMapTemp.put("category", new ProjectVoucherCategory().PHARMACY_ESTIMATE_BILL);
                        }

                        new RptSaleBillRegister(swiMain, paramHashMapTemp);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_OPENING_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptMedicineOpeningRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_RECEIVE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptMedicineReceiveRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_ISSUE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptMedicineIssueRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_TRANSFER_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptMedicineTransferRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_STOCK_LEDGER.caption)) {
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_STOCK_SUMMARY.caption)) {
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_BATCH_WISE_STOCK_SUMMARY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptBatchWiseStockSummary(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SUNDRY_DEBTOR_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new VendorMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_EXPIRY_REGISTER.caption)) {
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

                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                            paramHashMapTemp.put("flagOpenAsPopup", false);
                        }
                        new RptExpiryRegister(paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
//            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_REQUIREMENT_REGISTER.caption)) {
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
//
//                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
//                            paramHashMapTemp.put("flagOpenAsPopup", false);
//                        }
//                        new RptRequirementRegister(paramHashMapTemp, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_REQUIREMENT.caption)) {
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

                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                            paramHashMapTemp.put("flagOpenAsPopup", false);
                        }
                        new MedicineRequirement(paramHashMapTemp, swiMain);
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
                        new PurchaseBillApproval(menuPermission, paramHashMap, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STOCK_SUMMARY_DISPLAY.caption)) {
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

                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                            paramHashMapTemp.put("flagOpenAsPopup", false);
                        }
                        new StockSummary(menuPermission, paramHashMapTemp, swiMain);
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

                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                            paramHashMapTemp.put("flagOpenAsPopup", false);
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

                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                            paramHashMapTemp.put("flagOpenAsPopup", false);
                        }

                        new StockBatch(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_REQUISITION_DISPLAY.caption)) {
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

                        new MedicineRequisitionDisplay(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_INDENT_DISPLAY.caption)) {
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

                        new MedicineIndentnDisplay(menuPermission, swiMain);
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
                        new ProcurementPlannerDisplay(menuPermission, paramHashMapTemp, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GST_SALE_BILL_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptGSTSaleRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHARMACY_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPharmacyLedgerRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHARMACY_OUTSTANDING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPharmacyOutstandingRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.RESTRICTED_GROUP_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptRestrictGroup(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_SEARCH.caption)) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineSearch(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GROUP_WISE_PURCHASE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptGroupWisePurchaseRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GROUP_WISE_SALE_REGISTER.caption)) {
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

                        if (!paramHashMapTemp.containsKey("category")) {
                            paramHashMapTemp.put("category", new ProjectVoucherCategory().PHARMACY_BILL + "','" + new ProjectVoucherCategory().PHARMACY_BILL_CASH);
                        }

                        new RptGroupWiseSaleRegister(swiMain, paramHashMapTemp);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTANT_WISE_SALE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptConsultantWiseSale(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_DISCOUNT_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptSaleDiscountRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_FINAL_DISCOUNT_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptSaleFinalDiscountRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NEW_MEDICINE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptNewMedicineRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DRUG_FORMULARY_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptDrugFormularyRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NON_MOVING_MEDICINE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptNonMovingMedicineRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PRESCRIPTION_AND_SALE_COMPARISION_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPrescriptionSaleComparisionRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.RESTRICTED_GROUP_MEDICINE_LIST.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptRestrictedGroupMedicineList(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_RATE_SCHEME_CHANGE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptMedicineRateSchemeChangeRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ABC_ANALYSIS.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptABCAnalysisRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENVIRONMENT_SETTING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new EnvironmentSettingSite(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ORGANIZATION_BILLING_ON_RATE_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new OrganizationBillingOnRateMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_DEPARTMENT_INFO.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineDepartmentInfo(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_MODIFICATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MedicineModification(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CHANGE_PAYMENT_SOURCE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ChangePaymentSource(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_WISE_PROFITABILITY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptItemWiseProfitability(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALE_BILL_RETURN_PROVISIONAL.caption)) {
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
                            paramHashMapTemp.put("widgetName", new pmPharmacy().SALE_BILL_RETURN_PROVISIONAL.caption);
                        }

                        if (!paramHashMapTemp.containsKey("vCategory")) {
                            paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().MEDICINE_RETURN_REQUEST);
                        }

                        new SaleBillReturnProvisional(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PENDING_GRN_CONTROL_AC.caption)) {
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Medicine_Stock_Status.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPharmacyStkReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALES_VS_PURCHASE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptSalesPurchase(swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PENDING_PURCHASE_ORDER_DISPLAY.caption)) {
                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }
                if (!paramHashMapTemp.containsKey("module")) {
                    paramHashMapTemp.put("module", pmMenu.moduleName);
                }
                new PendingPO_Display(menuPermission, paramHashMapTemp, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_BILL_SETTELEMEMT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new WholeSaleBillSettelement(menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_PAYMENT_RECIPT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new WholeSalePaymentRecipt(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_BILL_RETURN.caption)) {
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
                            paramHashMapTemp.put("widgetName", new pmPharmacy().WHOLE_SALE_BILL_RETURN.caption);
                        }

                        if (!paramHashMapTemp.containsKey("vCategory")) {
                            paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().WHOLE_SALE_RETURN_CREDIT);
                        }

                        new WholeSaleBillReturn(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_BILL_REGISTER.caption)) {
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

                        if (!paramHashMapTemp.containsKey("category")) {
                            paramHashMapTemp.put("category", new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CASH + "','" + new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CREDIT);
                        }

                        new RptWholeSaleBillRegister(swiMain, paramHashMapTemp);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_BILL_RETURN_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptWholeSaleBillReturnRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_STOCK_SUMMARY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptWholeSaleStockSummary(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_STOCK_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptWholeSaleStockLedger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_ENVIRONMENT_SETTING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new WholeSaleSetting(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.VEHICLE_TYPE_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new VehicleTypeMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_WISE_OUTSTANDING_DEBTORS.caption)) {
                new BillWiseOutstandingDebtors(new HashMap(), swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EGST_PARAM.caption)) {
                new EgstParam(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ADDITIONAL_PLACE_OF_BUSINESS.caption)) {
                new AdditionalPlaceOfBusiness(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CD_CONFIGURATION_REPORT.caption)) {
                new RptCdConfiguration(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_GATE_PASS_OUT.caption)) {
                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }

                if (!paramHashMapTemp.containsKey("widgetName")) {
                    paramHashMapTemp.put("widgetName", new pmPharmacy().WHOLE_SALE_GATE_PASS_OUT.caption);
                }
                if (!paramHashMapTemp.containsKey("callFrom")) {
                    paramHashMapTemp.put("callFrom", new pmPharmacy().WHOLE_SALE_GATE_PASS_OUT.caption);
                }

                if (!paramHashMapTemp.containsKey("vCategory")) {
                    paramHashMapTemp.put("vCategory", new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CASH);
                    paramHashMapTemp.put("vCategory1", new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CREDIT);
                }

                new SaleBillWholeSale(menuPermission, paramHashMapTemp, swiMain);
                ppCallBackWait.hide(true);

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PAYMENT_RECIPT_REGISTER.caption)) {
                new RptPaymentReceiptRegister(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CUSTOMER_CREDIT_LIMIT_BALANCE_REGISTER.caption)) {
                new CustomerCreditLimitBalanceRegister(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFORMA_PURCHASE_RETURN.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ProformaPurchaseReturn(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFORMA_PURCHASE_RETURN.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ProformaPurchaseReturn(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFORMA_PURCHASE_RETURN_REGISTER.caption)) {
                new RptProformaPurchaseReturnRegister(swiMain);
                ppCallBackWait.hide(true);
            } else {
                ppCallBackWait.hide(true);
            }

        } catch (Exception e) {
            ppCallBackWait.hide(true);
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

}
