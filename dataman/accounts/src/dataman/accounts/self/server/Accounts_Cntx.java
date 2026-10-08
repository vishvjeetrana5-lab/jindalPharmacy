package dataman.accounts.self.server;

import dataman.accounts.acDisplaySetting.client.AcDisplaySetting;
import dataman.accounts.acDisplaySetting.client.AcDisplaySettingComparison;
import dataman.accounts.acDisplaySetting.server.AcDisplaySetting_SrvImpl;
import dataman.accounts.acGroupDetail.client.AcGroupDetail;
import dataman.accounts.acGroupDetail.server.AcGroupDetail_SrvImpl;
import dataman.accounts.acGroupDetailCompare.client.AcGroupDetailCompare;
import dataman.accounts.acGroupDetailCompare.server.AcGroupDetailCompare_SrvImpl;
import dataman.accounts.acLedgerDetail.client.AcLedgerDetail;
import dataman.accounts.acLedgerDetail.server.AcLedgerDetail_SrvImpl;
import dataman.accounts.accountGroup.client.AccountGroup;
import dataman.accounts.accountGroup.server.AccountGroup_SrvImpl;
import dataman.accounts.accountMaster.client.AccountMaster;
import dataman.accounts.accountMaster.server.AccountMaster_SrvImpl;
import dataman.accounts.accountOpeningEntry.client.AccountOpeningEntry;
import dataman.accounts.accountOpeningEntry.server.AccountOpeningEntry_SrvImpl;
import dataman.accounts.additionalPlaceOfBusiness.client.AdditionalPlaceOfBusiness;
import dataman.accounts.additionalPlaceOfBusiness.server.AdditionalPlaceOfBusiness_SrvImpl;
import dataman.accounts.addressBook.client.AddressBook;
import dataman.accounts.addressBook.server.AddressBook_SrvImpl;
import dataman.accounts.advanceEntry.client.AdvanceEntry;
import dataman.accounts.advanceEntry.server.AdvanceEntry_SrvImpl;
import dataman.accounts.advanceRequestEntry.client.AdvanceRequestEntry;
import dataman.accounts.advanceRequestEntry.server.AdvanceRequestEntry_SrvImpl;
import dataman.accounts.balanceSheet.client.BalanceSheet;
import dataman.accounts.balanceSheet.server.BalanceSheet_SrvImpl;
import dataman.accounts.balanceSheetCompare.client.BalanceSheetDisplayCompare;
import dataman.accounts.balanceSheetCompare.server.BalanceSheetDisplayCompare_SrvImpl;
import dataman.accounts.bankAccountDetail.client.BankAccountDetail;
import dataman.accounts.bankAccountDetail.server.BankAccountDetail_SrvImpl;
import dataman.accounts.bankLetterApproval.client.BankLetterApproval;
import dataman.accounts.bankLetterApproval.server.BankLetterApproval_SrvImpl;
import dataman.accounts.bankLetterCreation.client.BankLetterCreation;
import dataman.accounts.bankLetterCreation.server.BankLetterCreation_SrvImpl;
import dataman.accounts.bankReconciliationEntry.client.BankReconciliationEntry;
import dataman.accounts.bankReconciliationEntry.server.BankReconciliationEntry_SrvImpl;
import dataman.accounts.billHandle.client.BillHandle;
import dataman.accounts.billHandle.server.BillHandle_SrvImpl;
import dataman.accounts.billHandleApproval.client.BillApproval;
import dataman.accounts.billHandleApproval.server.BillApproval_SrvImpl;
import dataman.accounts.bulkChequePrinting.client.BulkChequePrinting;
import dataman.accounts.bulkChequePrinting.server.BulkChequePrinting_SrvImpl;
import dataman.accounts.chequePrintSetup.client.ChequePrintSetup;
import dataman.accounts.chequePrintSetup.server.ChequePrintSetup_SrvImpl;
import dataman.accounts.cityMaster.client.CityMaster;
import dataman.accounts.cityMaster.server.CityMaster_SrvImpl;
import dataman.accounts.countryMaster.client.CountryMaster;
import dataman.accounts.countryMaster.server.CountryMaster_SrvImpl;
import dataman.accounts.currencyMaster.client.CurrencyMaster;
import dataman.accounts.currencyMaster.server.CurrencyMaster_SrvImpl;

import dataman.accounts.defineCostCenter.client.DefineCostCenter;
import dataman.accounts.defineCostCenter.server.DefineCostCenter_SrvImpl;
import dataman.accounts.budgetHeadMaster.client.BudgetHeadMaster;
import dataman.accounts.budgetHeadMaster.server.BudgetHeadMaster_SrvImpl;
import dataman.accounts.budgetAllocationEntry.client.BudgetAllocationEntry;
import dataman.accounts.budgetAllocationEntry.server.BudgetAllocationEntry_SrvImpl;
import dataman.accounts.budgetDisbursement.client.BudgetDisbursement;
import dataman.accounts.budgetDisbursement.server.BudgetDisbursement_SrvImpl;

import dataman.accounts.detailTrialBalance.client.DetailTrialBalance;
import dataman.accounts.detailTrialBalance.server.DetailTrialBalance_SrvImpl;
import dataman.accounts.enviroSettings.client.EnviroSettings;
import dataman.accounts.enviroSettings.server.EnviroSettings_SrvImpl;
import dataman.accounts.finalPaymentEntry.client.FinalPaymentEntry;
import dataman.accounts.finalPaymentEntry.server.FinalPaymentEntry_SrvImpl;
import dataman.accounts.finalPaymentRequestEntry.client.FinalPaymentRequestEntry;
import dataman.accounts.finalPaymentRequestEntry.server.FinalPaymentRequestEntry_SrvImpl;
import dataman.accounts.financialBookClose.client.FinancialBookClose;
import dataman.accounts.financialBookClose.server.FinancialBookClose_SrvImpl;
import dataman.accounts.fundInTransitEntry.client.FundInTransitEntry;
import dataman.accounts.fundInTransitEntry.server.FundInTransitEntry_SrvImpl;
import dataman.accounts.fundTransferEntry.client.FundTransferEntry;
import dataman.accounts.fundTransferEntry.server.FundTransferEntry_SrvImpl;
import dataman.accounts.gstVoucherEntry.client.GstVoucherEntry;
import dataman.accounts.gstVoucherEntry.server.GstVoucherEntry_SrvImpl;
import dataman.accounts.gstVoucherEntryApproval.client.GstVoucherEntryApproval;
import dataman.accounts.gstVoucherEntryApproval.server.GstVoucherEntryApproval_SrvImpl;
import dataman.accounts.ledgerGroup.client.LedgerGroup;
import dataman.accounts.ledgerGroup.server.LedgerGroup_SrvImpl;
import dataman.accounts.locationMaster.client.LocationMaster;
import dataman.accounts.locationMaster.server.LocationMaster_SrvImpl;
import dataman.accounts.nationalityMaster.client.NationalityMaster;
import dataman.accounts.nationalityMaster.server.NationalityMaster_SrvImpl;
import dataman.accounts.profitLossDisplay.client.ProfitLossDisplay;
import dataman.accounts.profitLossDisplay.server.ProfitLossDisplay_SrvImpl;
import dataman.accounts.profitLossDisplayCompare.client.ProfitLossDisplayCompare;
import dataman.accounts.profitLossDisplayCompare.server.ProfitLossDisplayCompare_SrvImpl;
import dataman.accounts.reports.client.AccountGroupMergeLedger;
import dataman.accounts.reports.client.AgeingAnalysisBillWise;
import dataman.accounts.reports.client.AgeingAnalysisFIFO;
import dataman.accounts.reports.client.Annexure;
import dataman.accounts.reports.client.AnnexureCC;
import dataman.accounts.reports.client.BankBook;
import dataman.accounts.reports.client.BillWiseOutstandingCreditors;
import dataman.accounts.reports.client.BillWiseOutstandingDebtors;
import dataman.accounts.reports.client.CashBook;
import dataman.accounts.reports.client.DailyTransBook;
import dataman.accounts.reports.client.DailyTransBookCC;
import dataman.accounts.reports.client.DayBook;
import dataman.accounts.reports.client.DayBookCC;
import dataman.accounts.reports.client.GTAReg;
import dataman.accounts.reports.client.GstVoucherEntryRegister;
import dataman.accounts.reports.client.JournalBook;
import dataman.accounts.reports.client.Ledger;
import dataman.accounts.reports.client.RptLedgerExcel;
import dataman.accounts.reports.client.BalanceConfirmation;
import dataman.accounts.reports.client.LedgerCC;
import dataman.accounts.reports.client.LedgerGroupMergeLedger;
import dataman.accounts.reports.client.MonthlyLedgerSummary;
import dataman.accounts.reports.client.MonthlyLedgerSummaryFull;
import dataman.accounts.reports.client.OutstandingCreditorsFIFO;
import dataman.accounts.reports.client.OutstandingDebtorsFIFO;
import dataman.accounts.reports.client.PartyWiseTDS;
import dataman.accounts.reports.client.PostingReport;
import dataman.accounts.reports.client.RptAccountGroupDetail;
import dataman.accounts.reports.client.RptAdvanceReport;
import dataman.accounts.reports.client.RptBalanceSheet;
import dataman.accounts.reports.client.RptBankLetterReport;
import dataman.accounts.reports.client.RptBankReconciliation;
import dataman.accounts.reports.client.RptBillHandleRegister;
import dataman.accounts.reports.client.RptBillPaymentMaster;
import dataman.accounts.reports.client.RptBudgetAnalysis;
import dataman.accounts.reports.client.RptDetailTrialBalance;
import dataman.accounts.reports.client.RptLedgerDisplay;
import dataman.accounts.reports.client.RptPaymentReport;
import dataman.accounts.reports.client.RptProfitLoss;
import dataman.accounts.reports.client.RptProjectedGSTRegister;
import dataman.accounts.reports.client.RptTrialBalance;
import dataman.accounts.reports.client.TDSCertificate;
import dataman.accounts.reports.client.TDSReport;
import dataman.accounts.reports.client.TrialDetail;
import dataman.accounts.reports.client.TrialDetailCC;
import dataman.accounts.reports.client.TrialDetailDrCr;
import dataman.accounts.reports.client.TrialDetailDrCrCC;
import dataman.accounts.reports.client.TrialGroup;
import dataman.accounts.reports.client.TrialGroupCC;
import dataman.accounts.reports.client.UnapprovedLedger;
import dataman.accounts.reports.server.AgeingAnalysisBillWise_SrvImpl;
import dataman.accounts.reports.server.AgeingAnalysisFIFO_SrvImpl;
import dataman.accounts.reports.server.AnnexureCC_SrvImpl;
import dataman.accounts.reports.server.Annexure_SrvImpl;
import dataman.accounts.reports.server.BankBook_SrvImpl;
import dataman.accounts.reports.server.BillWiseOutstandingCreditors_SrvImpl;
import dataman.accounts.reports.server.BillWiseOutstandingDebtors_SrvImpl;
import dataman.accounts.reports.server.CashBook_SrvImpl;
import dataman.accounts.reports.server.DailyTransBookCC_SrvImpl;
import dataman.accounts.reports.server.DailyTransBook_SrvImpl;
import dataman.accounts.reports.server.DayBookCC_SrvImpl;
import dataman.accounts.reports.server.DayBook_SrvImpl;
import dataman.accounts.reports.server.GTAReg_SrvImpl;
import dataman.accounts.reports.server.GstVoucherEntryRegister_SrvImpl;
import dataman.accounts.reports.server.JournalBook_SrvImpl;
import dataman.accounts.reports.server.LedgerCC_SrvImpl;
import dataman.accounts.reports.server.LedgerGroupMergeLedger_SrvImpl;
import dataman.accounts.reports.server.Ledger_SrvImpl;
import dataman.accounts.reports.server.RptLedgerExcel_SrvImpl;
import dataman.accounts.reports.server.BalanceConfirmation_SrvImpl;
import dataman.accounts.reports.server.MonthlyLedgerSummaryFull_SrvImpl;
import dataman.accounts.reports.server.MonthlyLedgerSummary_SrvImpl;
import dataman.accounts.reports.server.OutstandingCreditorsFIFO_SrvImpl;
import dataman.accounts.reports.server.OutstandingDebtorsFIFO_SrvImpl;
import dataman.accounts.reports.server.PartyWiseTDS_SrvImpl;
import dataman.accounts.reports.server.RptAccountGroupDetail_SrvImpl;
import dataman.accounts.reports.server.RptAdvanceReport_SrvImpl;
import dataman.accounts.reports.server.RptBalanceSheet_SrvImpl;
import dataman.accounts.reports.server.RptBankReconciliation_SrvImpl;
import dataman.accounts.reports.server.RptBillHandleRegister_SrvImpl;
import dataman.accounts.reports.server.RptBillPaymentMaster_SrvImpl;
import dataman.accounts.reports.server.RptDetailTrialBalance_SrvImpl;
import dataman.accounts.reports.server.RptLedgerDisplay_SrvImpl;
import dataman.accounts.reports.server.RptPaymentReport_SrvImpl;
import dataman.accounts.reports.server.RptProfitLoss_SrvImpl;
import dataman.accounts.reports.server.RptProjectedGSTRegister_SrvImpl;
import dataman.accounts.reports.server.RptTrialBalance_SrvImpl;
import dataman.accounts.reports.server.TDSCertificate_SrvImpl;
import dataman.accounts.reports.server.TDSReport_SrvImpl;
import dataman.accounts.reports.server.TrialDetailDrCrCC_SrvImpl;
import dataman.accounts.reports.server.TrialDetailCC_SrvImpl;
import dataman.accounts.reports.server.TrialDetailDrCr_SrvImpl;
import dataman.accounts.reports.server.TrialDetail_SrvImpl;
import dataman.accounts.reports.server.TrialGroupCC_SrvImpl;
import dataman.accounts.reports.server.TrialGroup_SrvImpl;
import dataman.accounts.reports.server.UnapprovedLedger_SrvImpl;
import dataman.accounts.self.shared.AcEnum;
import dataman.accounts.stateMaster.client.StateMaster;
import dataman.accounts.stateMaster.server.StateMaster_SrvImpl;
import dataman.accounts.tallyExportMasters.client.TallyExportMasters;
import dataman.accounts.tallyExportMasters.server.TallyExportMasters_SrvImpl;
import dataman.accounts.tallyExportVouchers.client.TallyExportVouchers;
import dataman.accounts.tallyExportVouchers.server.TallyExportVouchers_SrvImpl;
import dataman.accounts.tdsCategoryDescriptionMaster.client.TdsCategoryDescriptionMaster;
import dataman.accounts.tdsCategoryDescriptionMaster.server.TdsCategoryDescriptionMaster_SrvImpl;
import dataman.accounts.tdsCategoryMaster.client.TdsCategoryMaster;
import dataman.accounts.tdsCategoryMaster.server.TdsCategoryMaster_SrvImpl;
import dataman.accounts.trialBalance.client.TrialBalance;
import dataman.accounts.trialBalance.server.TrialBalance_SrvImpl;
import dataman.accounts.voucherAdjustmentEntry.client.VoucherAdjustmentEntry;
import dataman.accounts.voucherAdjustmentEntry.server.VoucherAdjustmentEntry_SrvImpl;
import dataman.accounts.voucherEntry.client.VoucherEntry;
import dataman.accounts.voucherEntry.server.VoucherEntry_SrvImpl;
import dataman.accounts.voucherEntryApproval.client.VoucherEntryApproval;
import dataman.accounts.voucherEntryApproval.server.VoucherEntryApproval_SrvImpl;

import dataman.accounts.voucherEntryPaymentJV.client.VoucherEntryPaymentJV;
import dataman.accounts.voucherEntryPaymentJV.server.VoucherEntryPaymentJV_SrvImpl;

import dataman.accounts.trialBalanceCompare.client.TrialBalanceCompare;
import dataman.accounts.trialBalanceCompare.server.TrialBalanceCompare_SrvImpl;

import dataman.accounts.reports.client.RptPurchaseRegister;
import dataman.accounts.reports.server.RptPaymentReport_SrvImpl;

import dataman.accounts.reports.client.TrialDetailDrCrExcel;
import dataman.accounts.reports.server.RptBudgetAnalysis_SrvImpl;
import dataman.accounts.reports.server.RptPurchaseRegister_SrvImpl;
import dataman.accounts.reports.server.TrialDetailDrCrExcel_SrvImpl;

import dataman.accounts.reports.client.RptGrnRegister;
import dataman.accounts.reports.client.RptSupplierMaster;
import dataman.accounts.reports.server.PostingReport_SrvImpl;
import dataman.accounts.reports.server.RptBankLetterReport_SrvImpl;
import dataman.accounts.reports.server.RptGrnRegister_SrvImpl;
import dataman.accounts.reports.server.RptSupplierMaster_SrvImpl;
import dataman.accounts.wholesaleBillSettlement.client.WholesaleBillSettlement;
import dataman.accounts.wholesaleBillSettlement.server.WholesaleBillSettlement_SrvImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class Accounts_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = StateMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StateMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

//            System.out.println("StateMaster");
            strPath = AcDisplaySetting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AcDisplaySetting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            //            System.out.println("StateMaster");
            strPath = DetailTrialBalance.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DetailTrialBalance_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialBalance.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialBalance_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AcGroupDetail.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AcGroupDetail_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AcGroupDetailCompare.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AcGroupDetailCompare_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AcLedgerDetail.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AcLedgerDetail_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BalanceSheet.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BalanceSheet_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BalanceSheetDisplayCompare.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BalanceSheetDisplayCompare_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AccountGroup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AccountGroup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AccountMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AccountMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AddressBook.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AddressBook_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DefineCostCenter.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DefineCostCenter_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BudgetHeadMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BudgetHeadMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BudgetDisbursement.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BudgetDisbursement_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BudgetAllocationEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BudgetAllocationEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FundTransferEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FundTransferEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FundInTransitEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FundInTransitEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TdsCategoryMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TdsCategoryMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CurrencyMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CurrencyMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CountryMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CountryMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CityMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CityMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BankReconciliationEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BankReconciliationEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AccountOpeningEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AccountOpeningEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VoucherEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VoucherEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EnviroSettings.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnviroSettings_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = NationalityMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, NationalityMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            //Reports
            strPath = DailyTransBook.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DailyTransBook_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DailyTransBookCC.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DailyTransBookCC_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = JournalBook.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, JournalBook_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DayBook.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DayBook_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Ledger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Ledger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialDetail.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialDetail_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialBalanceCompare.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialBalanceCompare_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LedgerGroupMergeLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LedgerGroupMergeLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AccountGroupMergeLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, dataman.accounts.reports.server.AccountGroupMergeLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialGroup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialGroup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = UnapprovedLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, UnapprovedLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialGroupCC.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialGroupCC_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LedgerCC.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LedgerCC_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DayBookCC.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DayBookCC_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialDetailCC.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialDetailCC_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialDetailDrCrCC.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialDetailDrCrCC_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AnnexureCC.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AnnexureCC_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GTAReg.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GTAReg_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MonthlyLedgerSummaryFull.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MonthlyLedgerSummaryFull_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MonthlyLedgerSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MonthlyLedgerSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TDSCertificate.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TDSCertificate_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TDSReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TDSReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PartyWiseTDS.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PartyWiseTDS_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialDetailDrCr.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialDetailDrCr_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Annexure.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Annexure_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AgeingAnalysisFIFO.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AgeingAnalysisFIFO_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AgeingAnalysisBillWise.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AgeingAnalysisBillWise_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BillWiseOutstandingCreditors.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillWiseOutstandingCreditors_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BillWiseOutstandingDebtors.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillWiseOutstandingDebtors_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OutstandingDebtorsFIFO.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OutstandingDebtorsFIFO_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OutstandingCreditorsFIFO.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OutstandingCreditorsFIFO_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BalanceConfirmation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BalanceConfirmation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptAccountGroupDetail.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptAccountGroupDetail_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptLedgerDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLedgerDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptTrialBalance.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptTrialBalance_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProfitLossDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProfitLossDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProfitLossDisplayCompare.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProfitLossDisplayCompare_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptBalanceSheet.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBalanceSheet_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptProfitLoss.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptProfitLoss_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VoucherEntryApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VoucherEntryApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptDetailTrialBalance.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDetailTrialBalance_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LedgerGroup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LedgerGroup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ChequePrintSetup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ChequePrintSetup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TdsCategoryDescriptionMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TdsCategoryDescriptionMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BankBook.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BankBook_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CashBook.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CashBook_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptBankReconciliation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBankReconciliation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BulkChequePrinting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BulkChequePrinting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BankAccountDetail.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BankAccountDetail_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GstVoucherEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GstVoucherEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VoucherEntryPaymentJV.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VoucherEntryPaymentJV_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GstVoucherEntryRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GstVoucherEntryRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptProjectedGSTRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptProjectedGSTRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VoucherAdjustmentEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VoucherAdjustmentEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GstVoucherEntryApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GstVoucherEntryApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TallyExportMasters.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TallyExportMasters_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TallyExportVouchers.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TallyExportVouchers_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AdvanceRequestEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AdvanceRequestEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AdvanceEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AdvanceEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FinalPaymentRequestEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FinalPaymentRequestEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FinalPaymentEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FinalPaymentEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            //
            strPath = RptBillPaymentMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBillPaymentMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptAdvanceReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptAdvanceReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPaymentReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPaymentReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            //Shifted Forms
            strPath = BillHandle.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillHandle_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BillApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LocationMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LocationMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptBillHandleRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBillHandleRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            //Excel Reports
            strPath = RptPurchaseRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PostingReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PostingReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TrialDetailDrCrExcel.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TrialDetailDrCrExcel_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptBudgetAnalysis.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBudgetAnalysis_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGrnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGrnRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptLedgerExcel.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLedgerExcel_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSupplierMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSupplierMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BankLetterCreation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BankLetterCreation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BankLetterApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BankLetterApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptBankLetterReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBankLetterReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AdditionalPlaceOfBusiness.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AdditionalPlaceOfBusiness_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FinancialBookClose.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FinancialBookClose_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WholesaleBillSettlement.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WholesaleBillSettlement_SrvImpl.class);
            dyn.addMapping("/" + strPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
