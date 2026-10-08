package dataman.accounts.self.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.acGroupDetail.client.AcGroupDetail;
import dataman.accounts.acGroupDetailCompare.client.AcGroupDetailCompare;
import dataman.accounts.acLedgerDetail.client.AcLedgerDetail;
import dataman.accounts.accountGroup.client.AccountGroup;
import dataman.accounts.accountMaster.client.AccountMaster;
import dataman.accounts.accountOpeningEntry.client.AccountOpeningEntry;
import dataman.accounts.additionalPlaceOfBusiness.client.AdditionalPlaceOfBusiness;
import dataman.accounts.addressBook.client.AddressBook;
import dataman.accounts.advanceEntry.client.AdvanceEntry;
import dataman.accounts.advanceRequestEntry.client.AdvanceRequestEntry;
import dataman.accounts.balanceSheet.client.BalanceSheet;
import dataman.accounts.balanceSheetCompare.client.BalanceSheetDisplayCompare;
import dataman.accounts.bankAccountDetail.client.BankAccountDetail;
import dataman.accounts.bankLetterApproval.client.BankLetterApproval;
import dataman.accounts.bankLetterCreation.client.BankLetterCreation;
import dataman.accounts.bankReconciliationEntry.client.BankReconciliationEntry;
import dataman.accounts.billHandle.client.BillHandle;
import dataman.accounts.billHandleApproval.client.BillApproval;
import dataman.accounts.bulkChequePrinting.client.BulkChequePrinting;
import dataman.accounts.chequePrintSetup.client.ChequePrintSetup;
import dataman.accounts.cityMaster.client.CityMaster;
import dataman.accounts.countryMaster.client.CountryMaster;
import dataman.accounts.currencyMaster.client.CurrencyMaster;
import dataman.accounts.defineCostCenter.client.DefineCostCenter;
import dataman.accounts.budgetHeadMaster.client.BudgetHeadMaster;
import dataman.accounts.budgetAllocationEntry.client.BudgetAllocationEntry;
import dataman.accounts.budgetDisbursement.client.BudgetDisbursement;
import dataman.accounts.detailTrialBalance.client.DetailTrialBalance;
import dataman.accounts.trialBalanceCompare.client.TrialBalanceCompare;
import dataman.accounts.enviroSettings.client.EnviroSettings;
import dataman.accounts.finalPaymentEntry.client.FinalPaymentEntry;
import dataman.accounts.finalPaymentRequestEntry.client.FinalPaymentRequestEntry;
import dataman.accounts.financialBookClose.client.FinancialBookClose;
import dataman.accounts.fundInTransitEntry.client.FundInTransitEntry;
import dataman.accounts.fundTransferEntry.client.FundTransferEntry;
import dataman.accounts.gstVoucherEntry.client.GstVoucherEntry;
import dataman.accounts.gstVoucherEntryApproval.client.GstVoucherEntryApproval;
import dataman.accounts.ledgerGroup.client.LedgerGroup;
import dataman.accounts.locationMaster.client.LocationMaster;
import dataman.accounts.nationalityMaster.client.NationalityMaster;
import dataman.accounts.profitLossDisplay.client.ProfitLossDisplay;
import dataman.accounts.profitLossDisplayCompare.client.ProfitLossDisplayCompare;
import dataman.accounts.tdsCategoryMaster.client.TdsCategoryMaster;
import dataman.accounts.voucherEntry.client.VoucherEntry;
import dataman.accounts.voucherEntryPaymentJV.client.VoucherEntryPaymentJV;
import dataman.dmToolkit.singleLineMaster.client.SingleLineMaster;
import dataman.accounts.reports.client.*;
import dataman.accounts.stateMaster.client.StateMaster;
import dataman.accounts.tallyExportMasters.client.TallyExportMasters;
import dataman.accounts.tallyExportVouchers.client.TallyExportVouchers;
import dataman.accounts.tdsCategoryDescriptionMaster.client.TdsCategoryDescriptionMaster;
import dataman.accounts.trialBalance.client.TrialBalance;
import dataman.accounts.voucherAdjustmentEntry.client.VoucherAdjustmentEntry;
import dataman.accounts.voucherEntryApproval.client.VoucherEntryApproval;
import dataman.accounts.wholesaleBillSettlement.client.WholesaleBillSettlement;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;

import java.util.HashMap;
import java.util.List;

public class Accounts implements IsWidget {

    private lmAccounts pmMenu = new lmAccounts();
    private DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private DmMenuBar menu;

    public Accounts(final SetWidgetInvoker swiMain) {
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

            if (menuText.trim().equalsIgnoreCase(pmMenu.CHEQUE_PRINTING_SETUP.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ChequePrintSetup(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFIT_AND_LOSS_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ProfitLossDisplay(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROFIT_AND_LOSS_COMPARE_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ProfitLossDisplayCompare(menuPermission, swiMain, paramHashMap);

                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BALANCE_SHEET_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BalanceSheet(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BALANCE_SHEET_COMPARE_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BalanceSheetDisplayCompare(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_BALANCE_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialBalance(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DETAIL_TRIAL_BALANCE_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DetailTrialBalance(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DETAIL_TRIAL_BALANCE_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DetailTrialBalance(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.A_C_GROUP_DETAIL_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AcGroupDetail(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.A_C_GROUP_DETAIL_DISPLAY_COMPARISON.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AcGroupDetailCompare(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_BALANCE_COMPARISON.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialBalanceCompare(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEDGER_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AcLedgerDetail(menuPermission, swiMain, paramHashMap);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ACCOUNT_GROUP.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AccountGroup(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ACCOUNT_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AccountMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NATIONALITY_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
//                        new SingleLineMaster("Nationality", "nationalityMast", "Nationality", "Code", "Name", 50, false, false, SQLDataType.INT, menuPermission, swiMain);
                        new NationalityMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GROUP_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new LedgerGroup(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ZONE_MASTER.caption)) {
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
                        if (!paramHashMapTemp.containsKey("asPopup")) {
                            paramHashMapTemp.put("asPopup", false);
                        }
                        new SingleLineMaster(pmMenu.ZONE_MASTER.caption, "zoneMast", "Zone", "Code", "Name", 50, false, false, SQLDataType.INT, menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TDS_CATEGORY_DESCRIPTION_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
//                        new SingleLineMaster("TDS Category Description Master", "TDSCat_Description", "TDS Description", "Code", "Name", 25, false, false, SQLDataType.INT, menuPermission, swiMain);
                        new TdsCategoryDescriptionMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NARRATION_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Narration Master", "NarrationMast", "Narration", "Code", "Name", 100, false, false, SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ACCOUNT_OPENING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AccountOpeningEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PAYMENT_VOUCHER.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.RECEIPT_VOUCHER.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.JOURNAL_VOUCHER.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.CONTRA_VOUCHER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new VoucherEntry(menuPermission, swiMain, menuText);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GST_VOUCHER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GstVoucherEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.VOUCHER_ENTRY_PAYMENT_JV.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new VoucherEntryPaymentJV(menuPermission, swiMain, menuText);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CHEQUE_PRINTING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BulkChequePrinting(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.VOUCHER_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new VoucherEntryApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GST_VOUCHER_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GstVoucherEntryApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CITY_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new CityMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STATE_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
//                        new SingleLineMaster("State Master", "stateMast", "State Name", "Code", "Name", 25, false, false, SQLDataType.INT, menuPermission, swiMain);
                        new StateMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COUNTRY_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new CountryMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CURRENCY_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new CurrencyMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DEFINE_COSTCENTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DefineCostCenter(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BUDGET_HEAD_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BudgetHeadMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BUDGET_ALLOCATION_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BudgetAllocationEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BUDGET_DISBURSEMENT_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BudgetDisbursement(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ADDRESS_BOOK.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AddressBook(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BANK_ACCOUNT_DETAIL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BankAccountDetail(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FUND_TRANSFER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new FundTransferEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FUND_IN_TRANSIT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new FundInTransitEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TDS_CATEGORY_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TdsCategoryMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BANK_RECONCILIATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BankReconciliationEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENVIRONMENT_SETTINGS.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new EnviroSettings(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DAILY_TRANSACTION_SUMMARY.caption)) {
                //==================== Reports =================================
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DailyTransBook(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.POSTING_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PostingReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new Ledger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BANK_BOOK.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BankBook(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CASH_BOOK.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new CashBook(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.JOURNAL_BOOK.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new JournalBook(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DAY_BOOK.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DayBook(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_DETAIL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialDetail(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_GROUP.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialGroup(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEDGER_GROUP_MERGE_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new LedgerGroupMergeLedger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ACCOUNT_GROUP_MERGE_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AccountGroupMergeLedger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.UNAPPROVE_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new UnapprovedLedger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DAILY_TRANSACTION_SUMMARY_CC.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DailyTransBookCC(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_GROUP_CC.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialGroupCC(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEDGER_CC.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new LedgerCC(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DAY_BOOK_CC.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DayBookCC(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_DETAIL_CC.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialDetailCC(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_DETAIL_DR_CR_CC.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialDetailDrCrCC(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ANNEXURE_CC.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AnnexureCC(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.AGEING_AYALYSIS_BILL_WISE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AgeingAnalysisBillWise(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GTA_NON_GTA_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GTAReg(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.AGEING_AYALYSIS_FIFO.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AgeingAnalysisFIFO(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BALANCE_CONFIRMATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BalanceConfirmation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ANNEXURE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new Annexure(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_WISE_OUTSTANDING_CREDITORS.caption)) {
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

                        if (!paramHashMapTemp.containsKey("strReportFor")) {
                            paramHashMapTemp.put("strReportFor", "L");
                        }
                        new BillWiseOutstandingCreditors(paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_WISE_OUTSTANDING_DEBTORS.caption)) {
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

                        if (!paramHashMapTemp.containsKey("strReportFor")) {
                            paramHashMapTemp.put("strReportFor", "A");
                        }
                        new BillWiseOutstandingCreditors(paramHashMapTemp, swiMain);
//                        new BillWiseOutstandingDebtors(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MONTHLY_LEDGER_SUMMARY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MonthlyLedgerSummary(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MONTHLY_LEDGER_SUMMARY_FULL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new MonthlyLedgerSummaryFull(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TDS_CERTIFICATE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TDSCertificate(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TDS_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TDSReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PARTY_WISE_TDS.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PartyWiseTDS(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_DETAIL_DR_CR.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialDetailDrCr(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OUTSTANDING_DEBTORS_FIFO.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new OutstandingDebtorsFIFO(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OUTSTANDING_CREDITORS_FIFO.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new OutstandingCreditorsFIFO(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GST_VOUCHER_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GstVoucherEntryRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROJECTED_GST_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptProjectedGSTRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.VOUCHER_ADJUSTMENT_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new VoucherAdjustmentEntry(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Tally_Export_Masters.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TallyExportMasters(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Tally_Export_Vouchers.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TallyExportVouchers(menuPermission, swiMain);

                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ADVANCE_REQUEST_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AdvanceRequestEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ADVANCE_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AdvanceEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FINAL_PAYMENT_REQUEST_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new FinalPaymentRequestEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FINAL_PAYMENT_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new FinalPaymentEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } //
            else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_PAYMENT_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptBillPaymentMaster(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_PAYMENT_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptBillPaymentMaster(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ADVANCE_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptAdvanceReport(swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TRIAL_DETAIL_DR_CR_EXCEL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new TrialDetailDrCrExcel(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LOCATION_MASTER.caption)) {
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
                        new LocationMaster(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_HANDLE_APPROVAL.caption)) {
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
                        new BillApproval(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_HANDLE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new BillHandle(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ADDITIONAL_PLACE_OF_BUSINESS.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new AdditionalPlaceOfBusiness(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_STATUS_REPORT.caption)) {
                new RptBillHandleRegister(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BUDGET_ANALYSIS_REPORT.caption)) {
                new RptBudgetAnalysis(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GRN_REGISTER.caption)) {
                new RptGrnRegister(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEDGER_EXCEL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptLedgerExcel(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SUPPLIER_MASTER_REPORT.caption)) {
                new RptSupplierMaster(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BANK_LETTER_CREATION.caption)) {
                new BankLetterCreation(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BANK_LETTER_APPROVAL.caption)) {
                new BankLetterApproval(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BANK_LETTER_REPORT.caption)) {
                new RptBankLetterReport(swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FINANCIAL_BOOK_CLOSE.caption)) {
                new FinancialBookClose(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            }else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_BILL_SETTLEMENT.caption)) {
                 new WholesaleBillSettlement(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
            }else {
                ppCallBackWait.hide(true);
            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }
}
