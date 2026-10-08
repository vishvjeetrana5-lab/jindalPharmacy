package dataman.accounts.self.shared;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.accounts.accountOpeningEntry.client.AccountOpeningEntry;
import dataman.accounts.fundInTransitEntry.client.FundInTransitEntry;
import dataman.accounts.fundTransferEntry.client.FundTransferEntry;
import dataman.accounts.voucherEntry.client.VoucherEntry;
import dataman.accounts.voucherEntryPaymentJV.client.VoucherEntryPaymentJV;
import dataman.accounts.accountMaster.client.AccountMaster;
import dataman.accounts.advanceEntry.client.AdvanceEntry;
import dataman.accounts.bankLetterCreation.client.BankLetterCreation;
import dataman.accounts.billHandle.client.BillHandle;
import dataman.accounts.finalPaymentEntry.client.FinalPaymentEntry;
import dataman.accounts.financialBookClose.client.FinancialBookClose;
import dataman.dmBase.self.shared.UserMenu;
import java.util.ArrayList;
import java.util.List;

public class Accounts_Model {

    lmAccounts pmMenu = new lmAccounts();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Masters"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getManageAcconts(mainMenuList);

        menuList.add(new UserMenu("Transactions"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getManageTDS(mainMenuList);
        getManageBudget(mainMenuList);
        getTransaction(mainMenuList);
        getApprovals(mainMenuList);

        menuList.add(new UserMenu("Reports"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getDisplay(mainMenuList);
        getReportI(mainMenuList);
        getReportII(mainMenuList);
        getPaymentProcessReport(mainMenuList);
        getExcelsReport(mainMenuList);
        getBudgetReport(mainMenuList);

//        getReportCC(menuList);
        menuList.add(new UserMenu("Configs"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getGeneralSetup(mainMenuList);
        getTallyInterface(mainMenuList);

        menuList.add(new UserMenu("Advance & Bill Pay."));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getBillHandling(mainMenuList);
        getAdvanceBillPayment(mainMenuList);
        getBulkPayment(mainMenuList);

        return menuList;

    }

//<editor-fold defaultstate="collapsed" desc="Manage Accounts">
    private void getManageAcconts(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Manage Accounts"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.ACCOUNT_GROUP.caption));
        subMenuList.add(new UserMenu(pmMenu.GROUP_LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.ACCOUNT_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AccountMaster.detail);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AccountMaster.detail2);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AccountMaster.detail3);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AccountMaster.detail4);
        subMenuList.add(new UserMenu(pmMenu.ADDRESS_BOOK.caption));
        subMenuList.add(new UserMenu(pmMenu.BANK_ACCOUNT_DETAIL.caption));
        subMenuList.add(new UserMenu(pmMenu.LOCATION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ADDITIONAL_PLACE_OF_BUSINESS.caption));

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Manage TDS">
    private void getManageTDS(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Manage TDS"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.TDS_CATEGORY_DESCRIPTION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.TDS_CATEGORY_MASTER.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Manage Budget">
    private void getManageBudget(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Manage Budget"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.DEFINE_COSTCENTER.caption));

        subMenuList.add(new UserMenu(pmMenu.BUDGET_HEAD_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.BUDGET_ALLOCATION_ENTRY.caption));

        subMenuList.add(new UserMenu(pmMenu.BUDGET_DISBURSEMENT_ENTRY.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Transaction">
    private void getTransaction(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Transaction"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.ACCOUNT_OPENING.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AccountOpeningEntry.approvedByBlog);
        subMenuList.add(new UserMenu(pmMenu.PAYMENT_VOUCHER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(VoucherEntry.approvedByBlog);
        subMenuList.add(new UserMenu(pmMenu.RECEIPT_VOUCHER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(VoucherEntry.approvedByBlog);
        subMenuList.add(new UserMenu(pmMenu.JOURNAL_VOUCHER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(VoucherEntry.approvedByBlog);
        subMenuList.add(new UserMenu(pmMenu.CONTRA_VOUCHER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(VoucherEntry.approvedByBlog);
//        subMenuList.add(new UserMenu(pmMenu.VOUCHER_ENTRY_PAYMENT_JV.caption));
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(VoucherEntryPaymentJV.approvedByBlog);
        subMenuList.add(new UserMenu(pmMenu.GST_VOUCHER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(VoucherEntry.approvedByBlog);

        subMenuList.add(new UserMenu(pmMenu.BANK_RECONCILIATION.caption));
        subMenuList.add(new UserMenu(pmMenu.FUND_TRANSFER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(FundTransferEntry.approvedByBlog);

        subMenuList.add(new UserMenu(pmMenu.FUND_IN_TRANSIT.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(FundInTransitEntry.approvedByBlog);

        subMenuList.add(new UserMenu(pmMenu.CHEQUE_PRINTING.caption));
        subMenuList.add(new UserMenu(pmMenu.VOUCHER_ADJUSTMENT_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.FINANCIAL_BOOK_CLOSE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(FinancialBookClose.mnuLedgerPostingDisplayBlog);
        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_BILL_SETTLEMENT.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(FinalPaymentEntry.approvedByBlog);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Approvals">
    private void getApprovals(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Approvals"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.VOUCHER_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.GST_VOUCHER_APPROVAL.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Display">
    private void getDisplay(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Display"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        subMenuList.add(new UserMenu(pmMenu.TRIAL_BALANCE_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.A_C_GROUP_DETAIL_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.LEDGER_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.DETAIL_TRIAL_BALANCE_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.PROFIT_AND_LOSS_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.BALANCE_SHEET_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_BALANCE_COMPARISON.caption));
        subMenuList.add(new UserMenu(pmMenu.A_C_GROUP_DETAIL_DISPLAY_COMPARISON.caption));
        subMenuList.add(new UserMenu(pmMenu.PROFIT_AND_LOSS_COMPARE_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.BALANCE_SHEET_COMPARE_DISPLAY.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Report I">
    private void getReportI(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("General Report"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        subMenuList.add(new UserMenu(pmMenu.DAILY_TRANSACTION_SUMMARY.caption));
        subMenuList.add(new UserMenu(pmMenu.DAY_BOOK.caption));
        subMenuList.add(new UserMenu(pmMenu.BANK_BOOK.caption));
        subMenuList.add(new UserMenu(pmMenu.CASH_BOOK.caption));
        subMenuList.add(new UserMenu(pmMenu.JOURNAL_BOOK.caption));
        subMenuList.add(new UserMenu(pmMenu.LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_DETAIL.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_DETAIL_DR_CR.caption));
        subMenuList.add(new UserMenu(pmMenu.ANNEXURE.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_GROUP.caption));
        subMenuList.add(new UserMenu(pmMenu.LEDGER_GROUP_MERGE_LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.ACCOUNT_GROUP_MERGE_LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.UNAPPROVE_LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.MONTHLY_LEDGER_SUMMARY_FULL.caption));
        subMenuList.add(new UserMenu(pmMenu.MONTHLY_LEDGER_SUMMARY.caption));
        subMenuList.add(new UserMenu(pmMenu.TDS_CERTIFICATE.caption));
        subMenuList.add(new UserMenu(pmMenu.TDS_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.PARTY_WISE_TDS.caption));
        subMenuList.add(new UserMenu(pmMenu.GST_VOUCHER_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PROJECTED_GST_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.POSTING_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.SUPPLIER_MASTER_REPORT.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Report II">
    private void getReportII(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Report II"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.AGEING_AYALYSIS_FIFO.caption));
//        subMenuList.add(new UserMenu(pmMenu.AGEING_AYALYSIS_BILL_WISE.caption));
        subMenuList.add(new UserMenu(pmMenu.BILL_WISE_OUTSTANDING_DEBTORS.caption));
        subMenuList.add(new UserMenu(pmMenu.BILL_WISE_OUTSTANDING_CREDITORS.caption));
        subMenuList.add(new UserMenu(pmMenu.BALANCE_CONFIRMATION.caption));
//        subMenuList.add(new UserMenu(pmMenu.OUTSTANDING_DEBTORS_FIFO.caption));
//        subMenuList.add(new UserMenu(pmMenu.GTA_NON_GTA_REGISTER.caption));
//        subMenuList.add(new UserMenu(pmMenu.OUTSTANDING_CREDITORS_FIFO.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Report [CC]">
    private void getReportCC(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Report [CC]"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.DAILY_TRANSACTION_SUMMARY_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.DAY_BOOK_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.BANK_BOOK_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.CASH_BOOK_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.LEDGER_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_GROUP_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_DETAIL_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_DETAIL_DR_CR_CC.caption));
        subMenuList.add(new UserMenu(pmMenu.ANNEXURE_CC.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="General Setup">
    private void getGeneralSetup(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("General Setup"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.ENVIRONMENT_SETTINGS.caption));
        subMenuList.add(new UserMenu(pmMenu.CURRENCY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.COUNTRY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.STATE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.CITY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ZONE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.NATIONALITY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.NARRATION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.CHEQUE_PRINTING_SETUP.caption));

    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Tally Interface">

    private void getTallyInterface(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Tally Interface"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.Tally_Export_Masters.caption));
        subMenuList.add(new UserMenu(pmMenu.Tally_Export_Vouchers.caption));

    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="AdvanceBillPayemnt">

    private void getAdvanceBillPayment(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Advance Transaction"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.ADVANCE_REQUEST_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.ADVANCE_ENTRY.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AdvanceEntry.approvedByBlog);
        subMenuList.add(new UserMenu(pmMenu.FINAL_PAYMENT_REQUEST_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.FINAL_PAYMENT_ENTRY.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(FinalPaymentEntry.approvedByBlog);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Report II">
    private void getPaymentProcessReport(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Payment Process"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        subMenuList.add(new UserMenu(pmMenu.BILL_PAYMENT_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ADVANCE_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.PAYMENT_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.BILL_STATUS_REPORT.caption));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Excels">
    private void getExcelsReport(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Excel Reports"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        subMenuList.add(new UserMenu(pmMenu.PURCHASE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.TRIAL_DETAIL_DR_CR_EXCEL.caption));
        subMenuList.add(new UserMenu(pmMenu.GRN_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.LEDGER_EXCEL.caption));

    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Excels">

    private void getBudgetReport(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Budget Reports"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        subMenuList.add(new UserMenu(pmMenu.BUDGET_ANALYSIS_REPORT.caption));

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBillHandling">
    private void getBillHandling(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Bill Handle Over"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.BILL_HANDLE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(BillHandle.approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(BillHandle.isCheckPermissionBlog);
        subMenuList.add(new UserMenu(pmMenu.BILL_HANDLE_APPROVAL.caption));

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBulkPayment">
    private void getBulkPayment(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Bulk Payment"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.BANK_LETTER_CREATION.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(BankLetterCreation.approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(BankLetterCreation.isCheckPermissionBlog);
        subMenuList.add(new UserMenu(pmMenu.BANK_LETTER_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.BANK_LETTER_REPORT.caption));

    }
//</editor-fold>

}
