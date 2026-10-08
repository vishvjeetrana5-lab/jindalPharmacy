package aarogya.pharmacy.self.server;

import aarogya.pharmacy.addBatchSingle.client.AddBatchSingle;
import aarogya.pharmacy.addBatchSingle.server.AddBatchSingle_SrvImpl;
import aarogya.pharmacy.additionalPlaceOfBusiness.client.AdditionalPlaceOfBusiness;
import aarogya.pharmacy.additionalPlaceOfBusiness.server.AdditionalPlaceOfBusiness_SrvImpl;
import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.server.BatchNo_SrvImpl;
import aarogya.pharmacy.changePaymentSource.client.ChangePaymentSource;
import aarogya.pharmacy.changePaymentSource.server.ChangePaymentSource_SrvImpl;
import aarogya.pharmacy.customerMaster.client.CustomerMaster;
import aarogya.pharmacy.customerMaster.server.CustomerMaster_SrvImpl;
import aarogya.pharmacy.egstParam.client.EgstParam;
import aarogya.pharmacy.egstParam.server.EgstParam_SrvImpl;
import aarogya.pharmacy.employeeMaster.client.EmployeeMaster;
import aarogya.pharmacy.employeeMaster.server.EmployeeMaster_SrvImpl;
import aarogya.pharmacy.enquiry.client.Enquiry;
import aarogya.pharmacy.enquiry.server.Enquiry_SrvImpl;
import aarogya.pharmacy.enquiryApproval.client.EnquiryApproval;
import aarogya.pharmacy.enquiryApproval.server.EnquiryApproval_SrvImpl;
import aarogya.pharmacy.environmentSettingSite.client.EnvironmentSettingSite;
import aarogya.pharmacy.environmentSettingSite.server.EnvironmentSettingSite_SrvImpl;
import aarogya.pharmacy.expiryModification.client.ExpiryModification;
import aarogya.pharmacy.expiryModification.server.ExpiryModification_SrvImpl;
import aarogya.pharmacy.d365StockRecive.client.GRN;
import aarogya.pharmacy.d365StockRecive.server.GRN_SrvImpl;
import aarogya.pharmacy.grnApproval.client.GrnApproval;
import aarogya.pharmacy.grnApproval.server.GrnApproval_SrvImpl;
import aarogya.pharmacy.grnSearch.client.GRNSearch;
import aarogya.pharmacy.grnSearch.server.GRNSearch_SrvImpl;
import aarogya.pharmacy.indent.client.Indent;
import aarogya.pharmacy.indent.server.Indent_SrvImpl;
import aarogya.pharmacy.indentApproval.client.IndentApproval;
import aarogya.pharmacy.indentApproval.server.IndentApproval_SrvImpl;
import aarogya.pharmacy.indentClose.client.IndentClose;
import aarogya.pharmacy.indentClose.server.IndentClose_SrvImpl;
import aarogya.pharmacy.indentSearch.client.IndentSearch;
import aarogya.pharmacy.indentSearch.server.IndentSearch_SrvImpl;
import aarogya.pharmacy.irpInfoPopup.client.IrpInfoPopup;
import aarogya.pharmacy.irpInfoPopup.server.IrpInfoPopup_SrvImpl;
import aarogya.pharmacy.ledgerMaster.client.LedgerMaster;
import aarogya.pharmacy.ledgerMaster.server.LedgerMaster_SrvImpl;
import aarogya.pharmacy.ledgerPopup.client.LedgerPopup;
import aarogya.pharmacy.ledgerPopup.server.LedgerPopup_SrvImpl;
import aarogya.pharmacy.medicineCategoryMaster.client.MedicineCategoryMaster;
import aarogya.pharmacy.medicineCategoryMaster.server.MedicineCategoryMaster_SrvImpl;
import aarogya.pharmacy.materialCategoryMast.client.MaterialCategoryMast;
import aarogya.pharmacy.materialCategoryMast.server.MaterialCategoryMast_SrvImpl;
import aarogya.pharmacy.medicineCompositionMaster.client.MedicineCompositionMaster;
import aarogya.pharmacy.medicineCompositionMaster.server.MedicineCompositionMaster_SrvImpl;
import aarogya.pharmacy.medicineDepartmentInfo.client.MedicineDepartmentInfo;
import aarogya.pharmacy.medicineDepartmentInfo.server.MedicineDepartmentInfo_SrvImpl;
import aarogya.pharmacy.medicineGroupMaster.client.MedicineGroupMaster;
import aarogya.pharmacy.medicineGroupMaster.server.MedicineGroupMaster_SrvImpl;
import aarogya.pharmacy.medicineIssue.client.MedicineIssue;
import aarogya.pharmacy.vendorMaster.server.VendorMaster_SrvImpl;
import aarogya.pharmacy.vendorMaster.client.VendorMaster;
import aarogya.pharmacy.medicineIssue.server.MedicineIssue_SrvImpl;
import aarogya.pharmacy.medicineMakeMaster.client.MedicineMakeMaster;
import aarogya.pharmacy.medicineMakeMaster.server.MedicineMakeMaster_SrvImpl;
import aarogya.pharmacy.medicineMaster.client.MedicineMaster;
import aarogya.pharmacy.medicineMaster.server.MedicineMaster_SrvImpl;
import aarogya.pharmacy.medicineModification.client.MedicineModification;
import aarogya.pharmacy.medicineModification.server.MedicineModification_SrvImpl;
import aarogya.pharmacy.medicineOpening.client.MedicineOpening;
import aarogya.pharmacy.medicineOpening.server.MedicineOpening_SrvImpl;
import aarogya.pharmacy.medicineReceive.client.MedicineReceive;
import aarogya.pharmacy.medicineReceive.server.MedicineReceive_SrvImpl;
import aarogya.pharmacy.medicineRequirement.client.MedicineRequirement;
import aarogya.pharmacy.medicineRequirement.server.MedicineRequirement_SrvImpl;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.server.MedicineSearch_SrvImpl;
import aarogya.pharmacy.medicineTemplateMaster.client.MedicineTemplateMaster;
import aarogya.pharmacy.medicineTemplateMaster.server.MedicineTemplateMaster_SrvImpl;
import aarogya.pharmacy.medicineTransfer.client.MedicineTransfer;
import aarogya.pharmacy.medicineTransfer.server.MedicineTransfer_SrvImpl;
import aarogya.pharmacy.medicineTransferWholeSale.client.MedicineTransferWholeSale;
import aarogya.pharmacy.medicineTransferWholeSale.server.MedicineTransferWholeSale_SrvImpl;
import aarogya.pharmacy.medicineInTransit.client.MedicineInTransit;
import aarogya.pharmacy.medicineInTransit.server.MedicineInTransit_SrvImpl;
import aarogya.pharmacy.operationMediMaster.client.OperationMediMaster;
import aarogya.pharmacy.operationMediMaster.server.OperationMediMaster_SrvImpl;
import aarogya.pharmacy.orderSearch.client.OrderSearch;
import aarogya.pharmacy.orderSearch.server.OrderSearch_SrvImpl;
import aarogya.pharmacy.organizationBillingOnRateMaster.client.OrganizationBillingOnRateMaster;
import aarogya.pharmacy.organizationBillingOnRateMaster.server.OrganizationBillingOnRateMaster_SrvImpl;
import aarogya.pharmacy.partySearch.client.PartySearch;
import aarogya.pharmacy.partySearch.server.PartySearch_SrvImpl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.client.PharmacyFinalDiscountEntry;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.server.PharmacyFinalDiscountEntry_SrvImpl;
import aarogya.pharmacy.pharmacyReceipt.client.PharmacyReceipt;
import aarogya.pharmacy.pharmacyReceipt.server.PharmacyReceipt_SrvImpl;
import aarogya.pharmacy.pharmacyRefund.client.PharmacyRefund;
import aarogya.pharmacy.pharmacyRefund.server.PharmacyRefund_SrvImpl;
import aarogya.pharmacy.physicalStock.client.PhysicalStock;
import aarogya.pharmacy.physicalStock.server.PhysicalStock_SrvImpl;
import aarogya.pharmacy.physicalStockClearance.client.PhysicalStockClearance;
import aarogya.pharmacy.physicalStockClearance.server.PhysicalStockClearance_SrvImpl;
import aarogya.pharmacy.physicalStockPosting.client.PhysicalStockPosting;
import aarogya.pharmacy.physicalStockPosting.server.PhysicalStockPosting_SrvImpl;
import aarogya.pharmacy.profitSlab.client.ProfitSlab;
import aarogya.pharmacy.profitSlab.server.ProfitSlab_SrvImpl;
import aarogya.pharmacy.purchaseBill.client.PurchaseBill;
import aarogya.pharmacy.purchaseBill.server.PurchaseBill_SrvImpl;
import aarogya.pharmacy.purchaseBillApproval.client.PurchaseBillApproval;
import aarogya.pharmacy.purchaseBillApproval.server.PurchaseBillApproval_SrvImpl;
import aarogya.pharmacy.purchaseBillDirect.client.PurchaseBillDirect;
import aarogya.pharmacy.purchaseBillDirect.server.PurchaseBillDirect_SrvImpl;
import aarogya.pharmacy.purchaseOrder.client.PurchaseOrder;
import aarogya.pharmacy.purchaseOrder.server.PurchaseOrder_SrvImpl;
import aarogya.pharmacy.purchaseOrderApproval.client.PurchaseOrderApproval;
import aarogya.pharmacy.purchaseOrderApproval.server.PurchaseOrderApproval_SrvImpl;
import aarogya.pharmacy.purchaseReturn.client.PurchaseReturn;
import aarogya.pharmacy.purchaseReturn.server.PurchaseReturn_SrvImpl;
import aarogya.pharmacy.purchaseReturnApproval.client.PurchaseReturnApproval;
import aarogya.pharmacy.purchaseReturnApproval.server.PurchaseReturnApproval_SrvImpl;
import aarogya.pharmacy.quotation.client.Quotation;
import aarogya.pharmacy.quotation.server.Quotation_SrvImpl;
import aarogya.pharmacy.reports.client.*;
import aarogya.pharmacy.reports.server.*;
import aarogya.pharmacy.requisition.client.Requisition;
import aarogya.pharmacy.requisition.server.Requisition_SrvImpl;
import aarogya.pharmacy.requisitionApproval.client.RequisitionApproval;
import aarogya.pharmacy.requisitionApproval.server.RequisitionApproval_SrvImpl;
import aarogya.pharmacy.requisitionSearch.client.RequisitionSearch;
import aarogya.pharmacy.requisitionSearch.server.RequisitionSearch_SrvImpl;
import aarogya.pharmacy.saleBill.client.SaleBill;
import aarogya.pharmacy.saleBill.server.SaleBill_SrvImpl;
import aarogya.pharmacy.saleBillItemSearch.client.SaleBillItemSearch;
import aarogya.pharmacy.saleBillItemSearch.server.SaleBillItemSearch_SrvImpl;
import aarogya.pharmacy.saleBillReturn.client.SaleBillReturn;
import aarogya.pharmacy.saleBillReturn.server.SaleBillReturn_SrvImpl;
import aarogya.pharmacy.saleBillSearch.client.SaleBillSearch;
import aarogya.pharmacy.saleBillSearch.server.SaleBillSearch_SrvImpl;
import aarogya.pharmacy.schemeMaster.client.SchemeMaster;
import aarogya.pharmacy.schemeMaster.server.SchemeMaster_SrvImpl;
import aarogya.pharmacy.stockBatch.client.StockBatch;
import aarogya.pharmacy.stockBatch.server.StockBatch_SrvImpl;
import aarogya.pharmacy.stockLedger.client.StockLedger;
import aarogya.pharmacy.stockLedger.server.StockLedger_SrvImpl;
import aarogya.pharmacy.stockSummary.client.StockSummary;
import aarogya.pharmacy.stockSummary.server.StockSummary_SrvImpl;
import aarogya.pharmacy.walkInSearch.client.WalkInSearch;
import aarogya.pharmacy.walkInSearch.server.WalkInSearch_SrvImpl;
import aarogya.pharmacy.medicineRequisitionDisplay.client.MedicineRequisitionDisplay;
import aarogya.pharmacy.medicineRequisitionDisplay.server.MedicineRequisitionDisplay_SrvImpl;
import aarogya.pharmacy.medicineIndentDisplay.client.MedicineIndentnDisplay;
import aarogya.pharmacy.medicineIndentDisplay.server.MedicineIndentDisplay_SrvImpl;
import aarogya.pharmacy.medicineTransferApproval.client.MedicineTransferApproval;
import aarogya.pharmacy.medicineTransferApproval.server.MedicineTransferApproval_SrvImpl;
import aarogya.pharmacy.saleBillReturnProvisional.client.SaleBillReturnProvisional;
import aarogya.pharmacy.saleBillReturnProvisional.server.SaleBillReturnProvisional_SrvImpl;
import aarogya.pharmacy.walkInSearch.server.WalkInSearch_SrvImpl;
import aarogya.pharmacy.saleBillWholeSale.client.SaleBillWholeSale;
import aarogya.pharmacy.saleBillWholeSale.server.SaleBillWholeSale_SrvImpl;

import aarogya.pharmacy.procurementPlannerDisplay.client.ProcurementPlannerDisplay;
import aarogya.pharmacy.procurementPlannerDisplay.server.ProcurementPlannerDisplay_SrvImpl;

import aarogya.pharmacy.pendingPODisplay.client.PendingPO_Display;
import aarogya.pharmacy.pendingPODisplay.server.PendingPO_Display_SrvImpl;
import aarogya.pharmacy.proformaPurchaseReturn.client.ProformaPurchaseReturn;
import aarogya.pharmacy.proformaPurchaseReturn.server.ProformaPurchaseReturn_SrvImpl;
import aarogya.pharmacy.purchaseBillSearch.client.PurchaseBillSearch;
import aarogya.pharmacy.purchaseBillSearch.server.PurchaseBillSearch_SrvImpl;
import aarogya.pharmacy.purchaseOrderClose.client.PurchaseOrderClose;
import aarogya.pharmacy.purchaseOrderClose.server.PurchaseOrderClose_SrvImpl;
import aarogya.pharmacy.requisitionClose.client.RequisitionClose;
import aarogya.pharmacy.requisitionClose.server.RequisitionClose_SrvImpl;
import aarogya.pharmacy.vehicleTypeMaster.client.VehicleTypeMaster;
import aarogya.pharmacy.vehicleTypeMaster.server.VehicleTypeMaster_SrvImpl;
import aarogya.pharmacy.wholeSaleBillReturn.client.WholeSaleBillReturn;
import aarogya.pharmacy.wholeSaleBillReturn.server.WholeSaleBillReturn_SrvImpl;
import aarogya.pharmacy.wholeSaleBillSettelement.client.WholeSaleBillSettelement;
import aarogya.pharmacy.wholeSaleBillSettelement.server.WholeSaleBillSettelement_SrvImpl;
import aarogya.pharmacy.wholeSalePaymentRecipt.client.WholeSalePaymentRecipt;
import aarogya.pharmacy.wholeSalePaymentRecipt.server.WholeSalePaymentRecipt_SrvImpl;
import aarogya.pharmacy.wholeSaleReports.client.BillWiseOutstandingDebtors;
import aarogya.pharmacy.wholeSaleReports.client.CustomerCreditLimitBalanceRegister;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleBillRegister;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleBillReturnRegister;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleStockLedger;
import aarogya.pharmacy.wholeSaleReports.client.RptWholeSaleStockSummary;
import aarogya.pharmacy.wholeSaleReports.server.BillWiseOutstandingDebtors_SrvImpl;
import aarogya.pharmacy.wholeSaleReports.server.CustomerCreditLimitBalanceRegister_SrvImpl;
import aarogya.pharmacy.wholeSaleReports.server.RptWholeSaleBillRegister_SrvImpl;
import aarogya.pharmacy.wholeSaleReports.server.RptWholeSaleBillReturnRegister_SrvImpl;
import aarogya.pharmacy.wholeSaleReports.server.RptWholeSaleStockLedger_SrvImpl;
import aarogya.pharmacy.wholeSaleReports.server.RptWholeSaleStockSummary_SrvImpl;
import aarogya.pharmacy.wholeSaleSettings.client.WholeSaleSetting;
import aarogya.pharmacy.wholeSaleSettings.server.WholeSaleSetting_SrvImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class Pharmacy_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;
        try {

            strPath = MedicineTemplateMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineTemplateMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GRNSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GRNSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseBill.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseBill_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GRN.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GRN_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OrderSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OrderSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProformaPurchaseReturn.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProformaPurchaseReturn_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseReturn.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseReturn_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineCategoryMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineCategoryMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MaterialCategoryMast.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MaterialCategoryMast_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineGroupMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineGroupMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OperationMediMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OperationMediMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineMakeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineMakeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Indent.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Indent_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IndentApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IndentApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IndentClose.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IndentClose_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Enquiry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Enquiry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EnquiryApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnquiryApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Quotation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Quotation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EmployeeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EmployeeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SaleBill.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SaleBill_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PartySearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PartySearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Requisition.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Requisition_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndentEntryRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndentEntryRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPOReleaseAmountWithCD.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPOReleaseAmountWithCD_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseOrderApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseOrderApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IndentSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IndentSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseOrderReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseOrderReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseOrder.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseOrder_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineOpening.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineOpening_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineReceive.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineReceive_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineIssue.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineIssue_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineTransfer.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineTransfer_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineTransferApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineTransferApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineTransferWholeSale.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineTransferWholeSale_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineInTransit.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineInTransit_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PharmacyReceipt.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PharmacyReceipt_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGrnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGrnRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockBatch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockBatch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockLedgerDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockLedgerDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockSummaryDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockSummaryDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GrnApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GrnApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseBillApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseBillApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseReturnApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseReturnApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = StockBatch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StockBatch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineRequisitionDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineRequisitionDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineIndentnDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineIndentDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProcurementPlannerDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProcurementPlannerDisplay_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PendingPO_Display.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PendingPO_Display_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = StockSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StockSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptBatchWiseStockSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptBatchWiseStockSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = StockLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StockLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptMedicineReceiveRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptMedicineReceiveRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSaleBillRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSaleBillRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSaleBillCollection.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSaleBillCollection_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptEstimateRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptEstimateRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptRequistionRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptRequistionRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptMedicineOpeningRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptMedicineOpeningRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseBillDirect.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseBillDirect_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineCompositionMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineCompositionMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProfitSlab.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProfitSlab_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AddBatchSingle.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AddBatchSingle_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SaleBillReturn.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SaleBillReturn_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseReturnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseReturnRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptQuotation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptQuotation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptEnquiryReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptEnquiryReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BatchNo.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BatchNo_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptMedicineIssueRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptMedicineIssueRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptMedicineTransferRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptMedicineTransferRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSaleBillReturnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSaleBillReturnRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PharmacyRefund.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PharmacyRefund_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptExpiryRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptExpiryRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptRequirementRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptRequirementRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptRestrictGroup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptRestrictGroup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGSTPurchaseRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGSTPurchaseRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGSTSaleRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGSTSaleRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WalkInSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WalkInSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LedgerMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LedgerMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPharmacyLedgerRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPharmacyLedgerRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPharmacyOutstandingRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPharmacyOutstandingRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LedgerPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LedgerPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PhysicalStock.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PhysicalStock_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ExpiryModification.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ExpiryModification_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PhysicalStockPosting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PhysicalStockPosting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PhysicalStockClearance.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PhysicalStockClearance_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SaleBillItemSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SaleBillItemSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPhysicalStockBeforePosting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPhysicalStockBeforePosting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SchemeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SchemeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptOperationMediMast.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptOperationMediMast_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptConsultantWiseSale.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptConsultantWiseSale_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSaleDiscountRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSaleDiscountRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGroupWisePurchaseRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGroupWisePurchaseRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGroupWiseSaleRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGroupWiseSaleRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineRequirement.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineRequirement_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSaleFinalDiscountRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSaleFinalDiscountRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PharmacyFinalDiscountEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PharmacyFinalDiscountEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptRestrictedGroupMedicineList.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptRestrictedGroupMedicineList_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptNewMedicineRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptNewMedicineRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptDrugFormularyRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDrugFormularyRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptNonMovingMedicineRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptNonMovingMedicineRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPrescriptionSaleComparisionRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPrescriptionSaleComparisionRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptMedicineRateSchemeChangeRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptMedicineRateSchemeChangeRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptABCAnalysisRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptABCAnalysisRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EnvironmentSettingSite.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnvironmentSettingSite_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OrganizationBillingOnRateMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OrganizationBillingOnRateMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineDepartmentInfo.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineDepartmentInfo_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = MedicineModification.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, MedicineModification_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CustomerMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CustomerMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ChangePaymentSource.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ChangePaymentSource_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SaleBillSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SaleBillSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptItemWiseProfitability.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptItemWiseProfitability_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SaleBillWholeSale.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SaleBillWholeSale_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SaleBillReturnProvisional.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SaleBillReturnProvisional_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPendingGrnAcRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPendingGrnAcRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPharmacyStkReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPharmacyStkReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSalesPurchase.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSalesPurchase_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseBillValueRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseBillValueRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptProfitabilityReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptProfitabilityReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptProfitabilityReportM.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptProfitabilityReportM_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionClose.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionClose_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseOrderClose.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseOrderClose_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VendorMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VendorMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockValuation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockValuation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptCdConfiguration.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptCdConfiguration_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WholeSaleBillSettelement.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WholeSaleBillSettelement_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            dyn.addMapping("/" + strPath);

            strPath = WholeSalePaymentRecipt.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WholeSalePaymentRecipt_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WholeSaleBillReturn.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WholeSaleBillReturn_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptWholeSaleBillRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptWholeSaleBillRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptWholeSaleBillReturnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptWholeSaleBillReturnRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptWholeSaleStockLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptWholeSaleStockLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptWholeSaleStockSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptWholeSaleStockSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WholeSaleSetting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WholeSaleSetting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VehicleTypeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VehicleTypeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = BillWiseOutstandingDebtors.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, BillWiseOutstandingDebtors_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EgstParam.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EgstParam_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AdditionalPlaceOfBusiness.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AdditionalPlaceOfBusiness_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IrpInfoPopup.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IrpInfoPopup_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPaymentReceiptRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPaymentReceiptRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CustomerCreditLimitBalanceRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CustomerCreditLimitBalanceRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseBillSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseBillSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptProformaPurchaseReturnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptProformaPurchaseReturnRegister_SrvImpl.class);
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
