package aarogya.inventory.self.server;

import aarogya.inventory.divisionMaster.client.DivisionMaster;
import aarogya.inventory.divisionMaster.server.DivisionMaster_SrvImpl;
import aarogya.inventory.enquiry.client.Enquiry;
import aarogya.inventory.enquiry.server.Enquiry_SrvImpl;
import aarogya.inventory.enquiryApproval.client.EnquiryApproval;
import aarogya.inventory.enquiryApproval.server.EnquiryApproval_SrvImpl;
import aarogya.inventory.gateIn.client.GateIn;
import aarogya.inventory.gateIn.server.GateIn_SrvImpl;
import aarogya.inventory.gateInSearch.client.GateInSearch;
import aarogya.inventory.gateInSearch.server.GateInSearch_SrvImpl;
import aarogya.inventory.gateOut.client.GateOut;
import aarogya.inventory.gateOut.server.GateOut_SrvImpl;
import aarogya.inventory.gateOutSearch.client.GateOutSearch;
import aarogya.inventory.gateOutSearch.server.GateOutSearch_SrvImpl;
import aarogya.inventory.grn.client.Grn;
import aarogya.inventory.grn.server.Grn_SrvImpl;
import aarogya.inventory.grnApproval.client.GrnApproval;
import aarogya.inventory.grnApproval.server.GrnApproval_SrvImpl;
import aarogya.inventory.grnSearch.client.GrnSearch;
import aarogya.inventory.grnSearch.server.GrnSearch_SrvImpl;
import aarogya.inventory.indentApproval.client.IndentApproval;
import aarogya.inventory.indentApproval.server.IndentApproval_SrvImpl;
import aarogya.inventory.indentClose.client.IndentClose;
import aarogya.inventory.indentClose.server.IndentClose_SrvImpl;
import aarogya.inventory.indent.client.Indent;
import aarogya.inventory.indent.server.Indent_SrvImpl;
import aarogya.inventory.indentSearch.client.IndentSearch;
import aarogya.inventory.indentSearch.server.IndentSearch_SrvImpl;
import aarogya.inventory.itemCategoryMaster.client.ItemCategoryMaster;
import aarogya.inventory.itemCategoryMaster.server.ItemCategoryMaster_SrvImpl;
import aarogya.inventory.itemDepartmentInfo.client.ItemDepartmentInfo;
import aarogya.inventory.itemDepartmentInfo.server.ItemDepartmentInfo_SrvImpl;
import aarogya.inventory.itemGroupMaster.client.ItemGroupMaster;
import aarogya.inventory.itemGroupMaster.server.ItemGroupMaster_SrvImpl;
import aarogya.inventory.projectMaster.client.ProjectMaster;
import aarogya.inventory.projectMaster.server.ProjectMaster_SrvImpl;
import aarogya.inventory.itemMakeMaster.client.ItemMakeMaster;
import aarogya.inventory.itemMakeMaster.server.ItemMakeMaster_SrvImpl;
import aarogya.inventory.itemMaster.client.ItemMaster;
import aarogya.inventory.itemMaster.server.ItemMaster_SrvImpl;
import aarogya.inventory.orderSearch.client.OrderSearch;
import aarogya.inventory.orderSearch.server.OrderSearch_SrvImpl;
import aarogya.inventory.purchaseBill.client.PurchaseBill;
import aarogya.inventory.purchaseBill.server.PurchaseBill_SrvImpl;
import aarogya.inventory.purchaseBillApproval.client.PurchaseBillApproval;
import aarogya.inventory.purchaseBillApproval.server.PurchaseBillApproval_SrvImpl;
import aarogya.inventory.purchaseBillDirect.client.PurchaseBillDirect;
import aarogya.inventory.purchaseBillDirect.server.PurchaseBillDirect_SrvImpl;
import aarogya.inventory.workBillDirect.client.WorkBillDirect;
import aarogya.inventory.workBillDirect.server.WorkBillDirect_SrvImpl;
import aarogya.inventory.purchaseOrder.client.PurchaseOrder;
import aarogya.inventory.purchaseOrder.server.PurchaseOrder_SrvImpl;
import aarogya.inventory.workOrder.client.WorkOrder;
import aarogya.inventory.workOrder.server.WorkOrder_SrvImpl;
import aarogya.inventory.purchaseOrderApproval.client.PurchaseOrderApproval;
import aarogya.inventory.purchaseOrderApproval.server.PurchaseOrderApproval_SrvImpl;
import aarogya.inventory.purchaseOrderClose.client.PurchaseOrderClose;
import aarogya.inventory.purchaseOrderClose.server.PurchaseOrderClose_SrvImpl;
import aarogya.inventory.purchaseReturn.client.PurchaseReturn;
import aarogya.inventory.purchaseReturn.server.PurchaseReturn_SrvImpl;
import aarogya.inventory.purchaseReturnApproval.client.PurchaseReturnApproval;
import aarogya.inventory.purchaseReturnApproval.server.PurchaseReturnApproval_SrvImpl;
import aarogya.inventory.quotation.client.Quotation;
import aarogya.inventory.quotation.server.Quotation_SrvImpl;
import aarogya.inventory.reports.client.RptEnquiryReport;
import aarogya.inventory.reports.client.RptGrnRegister;
import aarogya.inventory.reports.client.RptIndent;
import aarogya.inventory.reports.client.RptPurchaseOrderReport;
import aarogya.inventory.reports.client.RptPurchaseRegister;
import aarogya.inventory.reports.client.RptQuotation;
import aarogya.inventory.reports.server.RptEnquiryReport_SrvImpl;
import aarogya.inventory.reports.server.RptGrnRegister_SrvImpl;
import aarogya.inventory.reports.server.RptIndent_SrvImpl;
import aarogya.inventory.reports.server.RptPurchaseOrderReport_SrvImpl;
import aarogya.inventory.reports.server.RptPurchaseRegister_SrvImpl;
import aarogya.inventory.reports.server.RptQuotation_SrvImpl;
import aarogya.inventory.unitMaster.client.UnitMaster;
import aarogya.inventory.unitMaster.server.UnitMaster_SrvImpl;
import aarogya.inventory.itemIssue.client.ItemIssue;
import aarogya.inventory.itemIssue.server.ItemIssue_SrvImpl;
import aarogya.inventory.itemOpening.client.ItemOpening;
import aarogya.inventory.itemOpening.server.ItemOpening_SrvImpl;
import aarogya.inventory.itemReceive.client.ItemReceive;
import aarogya.inventory.itemReceive.server.ItemReceive_SrvImpl;
import aarogya.inventory.wipToStockConversion.client.WipToStockConversion;
import aarogya.inventory.wipToStockConversion.server.WipToStockConversion_SrvImpl;
import aarogya.inventory.itemTransfer.client.ItemTransfer;
import aarogya.inventory.itemTransfer.server.ItemTransfer_SrvImpl;
import aarogya.inventory.manufacturerMaster.client.ManufacturerMaster;
import aarogya.inventory.manufacturerMaster.server.ManufacturerMaster_SrvImpl;
import aarogya.inventory.overHeadItemMaster.client.OverHeadItemMaster;
import aarogya.inventory.overHeadItemMaster.server.OverHeadItemMaster_SrvImpl;
import aarogya.inventory.physicalStock.client.PhysicalStock;
import aarogya.inventory.physicalStock.server.PhysicalStock_SrvImpl;
import aarogya.inventory.physicalStockClearance.client.PhysicalStockClearance;
import aarogya.inventory.physicalStockClearance.server.PhysicalStockClearance_SrvImpl;
import aarogya.inventory.physicalStockPosting.client.PhysicalStockPosting;
import aarogya.inventory.physicalStockPosting.server.PhysicalStockPosting_SrvImpl;
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
import aarogya.inventory.reports.client.RptStockBatch;
import aarogya.inventory.reports.client.RptStockLedger;
import aarogya.inventory.reports.client.RptStockLedgerDisplay;
import aarogya.inventory.reports.client.RptStockSummary;
import aarogya.inventory.reports.client.RptStockSummaryDisplay;
import aarogya.inventory.reports.server.RptExpiryRegister_SrvImpl;
import aarogya.inventory.reports.server.RptGSTPurchaseRegister_SrvImpl;
import aarogya.inventory.reports.server.RptItemIssueRegister_SrvImpl;
import aarogya.inventory.reports.server.RptItemOpeningRegister_SrvImpl;
import aarogya.inventory.reports.server.RptItemReceiveRegister_SrvImpl;
import aarogya.inventory.reports.server.RptItemTransferRegister_SrvImpl;
import aarogya.inventory.reports.server.RptNonMovingItemRegister_SrvImpl;
import aarogya.inventory.reports.server.RptPhysicalStockBeforePosting_SrvImpl;
import aarogya.inventory.reports.server.RptPurchaseReturnRegister_SrvImpl;
import aarogya.inventory.reports.server.RptRequistionRegister_SrvImpl;
import aarogya.inventory.reports.server.RptStockBatch_SrvImpl;
import aarogya.inventory.reports.server.RptStockLedgerDisplay_SrvImpl;
import aarogya.inventory.reports.server.RptStockLedger_SrvImpl;
import aarogya.inventory.reports.server.RptStockSummaryDisplay_SrvImpl;
import aarogya.inventory.reports.server.RptStockSummary_SrvImpl;
import aarogya.inventory.requisition.client.Requisition;
import aarogya.inventory.requisition.server.Requisition_SrvImpl;
import aarogya.inventory.requisitionApproval.client.RequisitionApproval;
import aarogya.inventory.requisitionApproval.server.RequisitionApproval_SrvImpl;
import aarogya.inventory.requisitionSearch.client.RequisitionSearch;
import aarogya.inventory.requisitionSearch.server.RequisitionSearch_SrvImpl;
import aarogya.inventory.requisitionClose.client.RequisitionClose;
import aarogya.inventory.requisitionClose.server.RequisitionClose_SrvImpl;
import aarogya.inventory.stockBatch.client.StockBatch;
import aarogya.inventory.stockBatch.server.StockBatch_SrvImpl;
import aarogya.inventory.purchaseOrderDisplay.client.PurchaseOrderDisplay;
import aarogya.inventory.purchaseOrderDisplay.server.PurchaseOrderDisplay_SrvImpl;
import aarogya.inventory.stockLedger.client.StockLedger;
import aarogya.inventory.stockLedger.server.StockLedger_SrvImpl;
import aarogya.inventory.stockSummary.client.StockSummary;
import aarogya.inventory.stockSummary.server.StockSummary_SrvImpl;
import aarogya.inventory.supplierMaster.client.SupplierMaster;
import aarogya.inventory.supplierMaster.server.SupplierMaster_SrvImpl;
import aarogya.inventory.gstServiceMaster.client.GstServiceMaster;
import aarogya.inventory.gstServiceMaster.server.GstServiceMaster_SrvImpl;
import aarogya.inventory.itemTransferApproval.client.ItemTransferApproval;
import aarogya.inventory.itemTransferApproval.server.ItemTransferApproval_SrvImpl;
import aarogya.inventory.reports.client.RptGatePassOut;
import aarogya.inventory.reports.client.RptPendingGrnAcRegister;
import aarogya.inventory.reports.server.RptGatePassOut_SrvImpl;
import aarogya.inventory.reports.server.RptPendingGrnAcRegister_SrvImpl;
import aarogya.inventory.visitorGatePass.client.VisitorGatePass;
import aarogya.inventory.visitorGatePass.server.VisitorGatePass_SrvImpl;
import aarogya.inventory.visitorGatePassSearch.client.VisitorGatePassSearch;
import aarogya.inventory.visitorGatePassSearch.server.VisitorGatePassSearch_SrvImpl;

import aarogya.inventory.projectIndicator.client.ProjectIndicator;
import aarogya.inventory.projectIndicator.server.ProjectIndicator_SrvImpl;

import aarogya.inventory.pendingPODisplay.client.PendingPO_Display;
import aarogya.inventory.pendingPODisplay.server.PendingPO_Display_SrvImpl;
import aarogya.inventory.reports.client.RptPOReleaseAmountWithCD;
import aarogya.inventory.reports.client.RptPurchaseBillValueRegister;
import aarogya.inventory.reports.client.RptStockValuation;
import aarogya.inventory.reports.server.RptPOReleaseAmountWithCD_SrvImpl;
import aarogya.inventory.reports.server.RptPurchaseBillValueRegister_SrvImpl;
import aarogya.inventory.reports.server.RptStockValuation_SrvImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class Inventory_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = Grn.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Grn_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OrderSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OrderSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseOrder.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseOrder_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WorkOrder.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WorkOrder_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = UnitMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, UnitMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemMakeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemMakeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Indent.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Indent_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IndentApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IndentApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemTransferApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemTransferApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IndentClose.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IndentClose_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Quotation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Quotation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemCategoryMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemCategoryMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemGroupMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemGroupMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProjectMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProjectMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverHeadItemMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverHeadItemMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Enquiry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Enquiry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = IndentSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, IndentSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EnquiryApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnquiryApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptIndent.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptIndent_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptEnquiryReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptEnquiryReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptQuotation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptQuotation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseOrderReport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseOrderReport_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseOrderApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseOrderApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseOrderClose.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseOrderClose_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseBill.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseBill_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseReturn.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseReturn_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GrnSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GrnSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGrnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGrnRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseReturnRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseReturnRegister_SrvImpl.class);
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

            strPath = PurchaseBillDirect.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseBillDirect_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WorkBillDirect.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WorkBillDirect_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemTransfer.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemTransfer_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemIssue.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemIssue_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemOpening.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemOpening_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemReceive.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemReceive_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WipToStockConversion.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WipToStockConversion_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = Requisition.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, Requisition_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RequisitionClose.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RequisitionClose_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = StockLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StockLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = StockSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StockSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = StockBatch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, StockBatch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PurchaseOrderDisplay.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PurchaseOrderDisplay_SrvImpl.class);
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

            strPath = RptItemReceiveRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptItemReceiveRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptRequistionRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptRequistionRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptItemOpeningRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptItemOpeningRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptItemIssueRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptItemIssueRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptItemTransferRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptItemTransferRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ManufacturerMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ManufacturerMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DivisionMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DivisionMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SupplierMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SupplierMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GstServiceMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GstServiceMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PhysicalStock.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PhysicalStock_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PhysicalStockPosting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PhysicalStockPosting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PhysicalStockClearance.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PhysicalStockClearance_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockSummary.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockSummary_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockLedger.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockLedger_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptExpiryRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptExpiryRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGSTPurchaseRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGSTPurchaseRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPhysicalStockBeforePosting.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPhysicalStockBeforePosting_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ItemDepartmentInfo.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ItemDepartmentInfo_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptNonMovingItemRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptNonMovingItemRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            //====================================
            strPath = GateOut.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GateOut_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VisitorGatePassSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VisitorGatePassSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = VisitorGatePass.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, VisitorGatePass_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GateInSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GateInSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptGatePassOut.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptGatePassOut_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            //
            strPath = GateIn.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GateIn_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GateOutSearch.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GateOutSearch_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPendingGrnAcRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPendingGrnAcRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProjectIndicator.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProjectIndicator_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PendingPO_Display.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PendingPO_Display_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPOReleaseAmountWithCD.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPOReleaseAmountWithCD_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptPurchaseBillValueRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptPurchaseBillValueRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptStockValuation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptStockValuation_SrvImpl.class);
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
