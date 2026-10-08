package aarogya.pharmacy.self.shared;

import aarogya.pharmacy.d365StockRecive.client.GRN;
import aarogya.pharmacy.indent.client.Indent;
import aarogya.pharmacy.medicineInTransit.client.MedicineInTransit;
import aarogya.pharmacy.medicineIssue.client.MedicineIssue;
import aarogya.pharmacy.medicineMaster.client.MedicineMaster;
import aarogya.pharmacy.medicineOpening.client.MedicineOpening;
import aarogya.pharmacy.medicineReceive.client.MedicineReceive;
import aarogya.pharmacy.medicineTransfer.client.MedicineTransfer;

import aarogya.pharmacy.pharmacyFinalDiscountEntry.client.PharmacyFinalDiscountEntry;
import aarogya.pharmacy.pharmacyReceipt.client.PharmacyReceipt;
import aarogya.pharmacy.pharmacyRefund.client.PharmacyRefund;
import aarogya.pharmacy.physicalStock.client.PhysicalStock;
import aarogya.pharmacy.physicalStockClearance.client.PhysicalStockClearance;
import aarogya.pharmacy.physicalStockPosting.client.PhysicalStockPosting;
import aarogya.pharmacy.purchaseBill.client.PurchaseBill;
import aarogya.pharmacy.purchaseBillDirect.client.PurchaseBillDirect;
import aarogya.pharmacy.purchaseOrder.client.PurchaseOrder;
import aarogya.pharmacy.purchaseReturn.client.PurchaseReturn;
import aarogya.pharmacy.quotation.client.Quotation;
import aarogya.pharmacy.requisition.client.Requisition;
import aarogya.pharmacy.saleBill.client.SaleBill;
import aarogya.pharmacy.saleBillWholeSale.client.SaleBillWholeSale;
import aarogya.pharmacy.saleBillReturn.client.SaleBillReturn;
import aarogya.pharmacy.medicineTransferWholeSale.client.MedicineTransferWholeSale;
import aarogya.pharmacy.proformaPurchaseReturn.client.ProformaPurchaseReturn;
import aarogya.pharmacy.purchaseOrderApproval.client.PurchaseOrderApproval;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.UserMenu;
import java.util.ArrayList;
import java.util.List;

public class Pharmacy_Model {

    private pmPharmacy pmMenu = new pmPharmacy();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Masters"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getGeneral(mainMenuList);

        menuList.add(new UserMenu("Transactions"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getGeneralTransaction(mainMenuList);
        getPurchase(mainMenuList);
        getSales(mainMenuList);
        getApprovals(mainMenuList);
        getStockAudit(mainMenuList);
        getStores(mainMenuList);

        menuList.add(new UserMenu("Reports"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getDisplay(mainMenuList);
        getReports(mainMenuList);
        getSaleReports(mainMenuList);
        getPurchaseReports(mainMenuList);

        menuList.add(new UserMenu("Configs"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getAdmin(mainMenuList);

        menuList.add(new UserMenu("Whole Sale"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getMaster(mainMenuList);
        getTransaction(mainMenuList);
        getReport(mainMenuList);
        getSettings(mainMenuList);

        return menuList;
    }

    private void getMaster(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Master"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.SUNDRY_DEBTOR_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.VENDOR_MASTER_ATTACH_FILE_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.VENDOR_MASTER_DELETE_FILE_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.VENDOR_MASTER_DETAIL_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.VENDOR_MASTER_DETAIL2_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.VENDOR_MASTER_DETAIL3_BLOG);

//        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_BILL_SETTELEMEMT.caption));
//
//        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_PAYMENT_RECIPT.caption));
        subMenuList.add(new UserMenu(pmMenu.VEHICLE_TYPE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ADDITIONAL_PLACE_OF_BUSINESS.caption));

    }

    private void getTransaction(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Transaction"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.SALE_BILL_WHOLE_SALE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.rateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.saleReturnBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.addBatchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.discountEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ctoAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ctrAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ctpAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.receiptAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.opdAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ipdAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.vendorAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.walkInAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ledgerAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_BILL_RETURN.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.rateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.discountEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.addBatchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ctoAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ctrAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ctpAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.receiptAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ledgerAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.paymentModeBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.medicineSearchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.uhidAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_GATE_PASS_OUT.caption));
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.rateEditableBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.saleReturnBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.dateEditableBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.departmentEditableBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.addBatchBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.discountEditableBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ctoAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ctrAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ctpAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.receiptAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.opdAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ipdAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.dayCareAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.vendorAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.walkInAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillWholeSale.ledgerAllowBlog);

    }

    private void getReport(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Report"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_BILL_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_BILL_RETURN_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_STOCK_LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_STOCK_SUMMARY.caption));
        subMenuList.add(new UserMenu(pmMenu.BILL_WISE_OUTSTANDING_DEBTORS.caption));
        subMenuList.add(new UserMenu(pmMenu.PAYMENT_RECIPT_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.CUSTOMER_CREDIT_LIMIT_BALANCE_REGISTER.caption));

    }

    private void getSettings(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Whole Sale Configs"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_ENVIRONMENT_SETTING.caption));
        subMenuList.add(new UserMenu(pmMenu.EGST_PARAM.caption));

    }

    private void getGeneralTransaction(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("General Transaction"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.REQUISITION.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(Requisition.approvedByBlog);

        subMenuList.add(new UserMenu(pmMenu.INDENT.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(Indent.approvedByBlog);

        subMenuList.add(new UserMenu(pmMenu.ENQUIRY.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(Indent.approvedByBlog);

        subMenuList.add(new UserMenu(pmMenu.QUOTATION.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(Quotation.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(Quotation.deleteFileBlog);

        subMenuList.add(new UserMenu(pmMenu.PURCHASE_ORDER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseOrder.approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseOrder.unApprovedByBlog);

        subMenuList.add(new UserMenu(pmMenu.EXPIRY_MODIFICATION.caption));

    }

//<editor-fold defaultstate="collapsed" desc="General">
    // public final static String medicineCompositionMaster = "Medicine Composition Master";
    //public final static String restrictedGroupMaster = "Restricted Group Master";
    //public final static String unitMaster = "Unit Master";
    private void getGeneral(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("General"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.MEDICINE_MAKE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.RESTRICTED_GROUP_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_COMPOSITION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_GROUP_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_CATEGORY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SUB_CATEGORY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_MODEL_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_STRENGTH_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MATERIAL_CATEGORY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MATERIAL_TYPE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.UNIT_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.MEDICINE_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineMaster.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineMaster.deleteFileBlog);

        subMenuList.add(new UserMenu(pmMenu.OPERATION_MEDICINE_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.CUSTOMER_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.CUSTOMER_MASTER_ATTACH_FILE_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.CUSTOMER_MASTER_DELETE_FILE_BLOG);

        subMenuList.add(new UserMenu(pmMenu.SUPPLIER_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.SUPPLIER_MASTER_ATTACH_FILE_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.SUPPLIER_MASTER_DELETE_FILE_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.SUPPLIER_MASTER_DETAIL_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.SUPPLIER_MASTER_DETAIL2_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.SUPPLIER_MASTER_DETAIL3_BLOG);

//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.SUPPLIER_MASTER_ATTACH_FILE_BLOG);
        subMenuList.add(new UserMenu(pmMenu.DIVISION_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.DIVISION_MASTER_ATTACH_FILE_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.DIVISION_MASTER_DELETE_FILE_BLOG);

        subMenuList.add(new UserMenu(pmMenu.MEDICINE_TEMPLATE_MASTER.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Purchase">
    private void getPurchase(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Purchase"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.GRN.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(GRN.approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(GRN.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(GRN.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(GRN.deleteFileBlog);

        subMenuList.add(new UserMenu(pmMenu.PURCHASE_BILL.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBill.approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBill.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBill.deleteFileBlog);

        subMenuList.add(new UserMenu(pmMenu.PURCHASE_BILL_DIRECT.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBillDirect.approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBillDirect.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBillDirect.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBillDirect.deleteFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseBillDirect.finalDisc);

        subMenuList.add(new UserMenu(pmMenu.PROFORMA_PURCHASE_RETURN.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ProformaPurchaseReturn.approvedByBlog);

        subMenuList.add(new UserMenu(pmMenu.PURCHASE_RETURN.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseReturn.approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseReturn.departmentEditableBlog);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Sales">
    private void getSales(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Sales"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.SALE_BILL.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.rateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.saleReturnBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.addBatchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.discountEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ctoAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ctrAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ctpAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.receiptAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ledgerAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.uhidAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.ESTIMATE_BILL.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.rateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.saleReturnBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.addBatchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.discountEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ledgerAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.uhidAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.ESTIMATE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.rateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.addBatchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.discountEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.ledgerAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBill.uhidAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.SALE_BILL_RETURN.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.rateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.discountEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.addBatchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ctoAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ctrAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ctpAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.receiptAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.ledgerAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.paymentModeBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.medicineSearchBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturn.uhidAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.SALE_FINAL_DISCOUNT_ENTRY.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyFinalDiscountEntry.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyFinalDiscountEntry.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyFinalDiscountEntry.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyFinalDiscountEntry.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyFinalDiscountEntry.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyFinalDiscountEntry.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyFinalDiscountEntry.ledgerAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.PHARMACY_RECEIPT.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyReceipt.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyReceipt.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyReceipt.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyReceipt.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyReceipt.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyReceipt.ledgerAllowBlog);

        subMenuList.add(new UserMenu(pmMenu.PHARMACY_REFUND.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyRefund.opdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyRefund.ipdAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyRefund.dayCareAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyRefund.emergencyAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyRefund.walkInAllowBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PharmacyRefund.ledgerAllowBlog);

//        subMenuList.add(new UserMenu(pmMenu.SALE_BILL_RETURN_PROVISIONAL.caption));
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.rateEditableBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.dateEditableBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.departmentEditableBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.addBatchBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.opdAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.ipdAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.dayCareAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.emergencyAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.walkInAllowBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(SaleBillReturnProvisional.ledgerAllowBlog);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Stores">
    private void getStores(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Stores"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.MEDICINE_OPENING.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineOpening.departmentEditableBlog);
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_RECEIVE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineReceive.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineReceive.dateEditableBlog);
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_ISSUE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineIssue.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineIssue.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineIssue.addBatchBlog);
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_TRANSFER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineTransfer.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineTransfer.dateEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineTransfer.approvedByBlog);
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_IN_TRANSIT.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineInTransit.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineInTransit.dateEditableBlog);
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_TRANSFER_WHOLESALE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineTransferWholeSale.departmentEditableBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(MedicineTransferWholeSale.dateEditableBlog);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Stock Audit">
    private void getStockAudit(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Stock Audit"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.PHYSICAL_STOCK.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PhysicalStock.departmentEditableBlog);

        subMenuList.add(new UserMenu(pmMenu.PHYSICAL_STOCK_POSTING.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PhysicalStockPosting.postedByBlog);

        subMenuList.add(new UserMenu(pmMenu.PHYSICAL_STOCK_CLEARANCE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PhysicalStockClearance.postedByBlog);

        subMenuList.add(new UserMenu(pmMenu.PHYSICAL_STOCK_BEFORE_POSTING_REPROT.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Approvals">
    private void getApprovals(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Approvals"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.REQUISITION_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.REQUISITION_CLOSE.caption));
        subMenuList.add(new UserMenu(pmMenu.INDENT_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.ENQUIRY_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.PURCHASE_ORDER_APPROVAL.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseOrderApproval.approvedBy);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PurchaseOrderApproval.unApprovedBy);
        subMenuList.add(new UserMenu(pmMenu.PURCHASE_ORDER_CLOSE.caption));
        subMenuList.add(new UserMenu(pmMenu.GRN_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.PURCHASE_BILL_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.PURCHASE_BILL_RETURN_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_TRANSFER_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.INDENT_CLOSE.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Display">
    private void getDisplay(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Display"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.STOCK_SUMMARY_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.STOCK_LEDGER_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.STOCK_BATCH_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_SEARCH.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_REQUIREMENT.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_REQUISITION_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_INDENT_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.PROCUREMENT_PLANNER_DISPLAY.caption));
        subMenuList.add(new UserMenu(pmMenu.PENDING_PURCHASE_ORDER_DISPLAY.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Reports">
    private void getReports(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("General Reports"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.REQUISITION_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.INDENT_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ENQUIRY_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.QUOTATION_REGISTER.caption));

        subMenuList.add(new UserMenu(pmMenu.MEDICINE_OPENING_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_RECEIVE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_ISSUE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_TRANSFER_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_IN_TRANSIT_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_STOCK_LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_STOCK_SUMMARY.caption));
        subMenuList.add(new UserMenu(pmMenu.ITEM_STOCK_VALUATION.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_BATCH_WISE_STOCK_SUMMARY.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_EXPIRY_REGISTER.caption));

//        subMenuList.add(new UserMenu(pmMenu.MEDICINE_REQUIREMENT_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.RESTRICTED_GROUP_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.RESTRICTED_GROUP_MEDICINE_LIST.caption));
        subMenuList.add(new UserMenu(pmMenu.NEW_MEDICINE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.DRUG_FORMULARY_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.NON_MOVING_MEDICINE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PRESCRIPTION_AND_SALE_COMPARISION_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ITEM_WISE_PROFITABILITY.caption));

        subMenuList.add(new UserMenu(pmMenu.PENDING_GRN_CONTROL_AC.caption));

        subMenuList.add(new UserMenu(pmMenu.SALES_VS_PURCHASE.caption));
        subMenuList.add(new UserMenu(pmMenu.Medicine_Stock_Status.caption));
        subMenuList.add(new UserMenu(pmMenu.PO_RELEASE_AMOUNT_WITH_CD.caption));
        subMenuList.add(new UserMenu(pmMenu.PB_Value_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PROFITABILITY_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.PROFITABILITY_REPORT_M.caption));

    }
//</editor-fold>

    private void getSaleReports(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Sale Reports"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.ESTIMATE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SALE_BILL_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SALE_BILL_COLLECTION_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ESTIMATE_BILL_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SALE_BILL_RETURN_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.GST_SALE_BILL_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PHARMACY_LEDGER.caption));
        subMenuList.add(new UserMenu(pmMenu.PHARMACY_OUTSTANDING.caption));
        subMenuList.add(new UserMenu(pmMenu.CONSULTANT_WISE_SALE.caption));
        subMenuList.add(new UserMenu(pmMenu.SALE_DISCOUNT_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SALE_FINAL_DISCOUNT_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ABC_ANALYSIS.caption));
        subMenuList.add(new UserMenu(pmMenu.GROUP_WISE_SALE_REGISTER.caption));
    }

    private void getPurchaseReports(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("Purchase Reports"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.PURCHASE_ORDER_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.GRN_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PURCHASE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PROFORMA_PURCHASE_RETURN_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PURCHASE_RETURN_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.GST_PURCHASE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.GROUP_WISE_PURCHASE_REGISTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_RATE_SCHEME_CHANGE_REGISTER.caption));
    }

    private void getAdmin(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Admin"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.ENVIRONMENT_SETTING.caption));

        subMenuList.add(new UserMenu(pmMenu.PROFIT_SLAB.caption));
        subMenuList.add(new UserMenu(pmMenu.IMPURITY_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.MANUFACTURER_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.MANUFACTURER_MASTER_ATTACH_FILE_BLOG);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(pmMenu.MANUFACTURER_MASTER_DELETE_FILE_BLOG);

        subMenuList.add(new UserMenu(pmMenu.LEDGER_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ORGANIZATION_BILLING_ON_RATE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_DEPARTMENT_INFO.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_MODIFICATION.caption));
        subMenuList.add(new UserMenu(pmMenu.CHANGE_PAYMENT_SOURCE.caption));
        subMenuList.add(new UserMenu(pmMenu.CD_CONFIGURATION_REPORT.caption));

    }
}
