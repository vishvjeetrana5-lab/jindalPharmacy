package aarogya.pharmacy.purchaseBillDirect.shared;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class PurchaseBillDirect_Vld {

    private static ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    public static void chkSave(PurchaseBillDirect_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode().trim(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 200);
        DmShared.isValidText(mrTemp.getSupplierTag(), "Supplier", 300);
        DmShared.isValidText(mrTemp.getSupplierBillNo(), "Ref. No.", 400);
        DmShared.isValidText(mrTemp.getSupplierBillDate(), "Ref. Date", 500);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department", 600);
        DmShared.isValidText(mrTemp.getCalculateOnTag(), "Calc. On", 650);
        DmShared.isValidText(mrTemp.getPaymentDueDate(), "Payment Due Date", 675);

        if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(mrTemp.getSupplierBillDate()).getTime()))) {
            throw new DmException(500, "Voucher Date should be greater than or equal to Ref. Date.");
        }

        if ((DmShared.convertDate(mrTemp.getPaymentDueDate()).getTime() < (DmShared.convertDate(mrTemp.getSupplierBillDate()).getTime()))) {
            throw new DmException(540, "Payment Due Date should be greater than or equal to Ref. Date.");
        }

        if (DmShared.vNull(mrTemp.getNetAmount()).doubleValue() < 0) {
            throw new DmException(0, "Net Amount can't be less than zero.");
        }

        boolean flag = false;
        String lineItemCodesCsv = "";
        String poItemChangeCondition = "";
        List< GrdGrnDetail_Mdl> listItemDetail = mrTemp.getGrdGrnDetail();
        for (GrdGrnDetail_Mdl detail_Mdl : listItemDetail) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                flag = true;

                /*==============================<< Create CSV for restricted department check >>===============================*/
                if (!lineItemCodesCsv.trim().equalsIgnoreCase("")) {
                    lineItemCodesCsv += ",";
                }
                lineItemCodesCsv += DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());

                /*==============================<< Create consition string for PO item change check >>===============================*/
                if (!poItemChangeCondition.trim().equalsIgnoreCase("")) {
                    poItemChangeCondition += " OR ";
                } else {
                    poItemChangeCondition += " WHERE ";
                }
                poItemChangeCondition += " (pod.docId = '" + DmShared.xNull(detail_Mdl.getPoDocId()) + "'";
                poItemChangeCondition += " AND pod.v_SNo = '" + DmShared.xNull(detail_Mdl.getPoV_SNo()) + "'";
                poItemChangeCondition += " AND pod.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                /*==============================<< Validate Line Items >>===============================*/
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Medicine in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase("")) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Unit in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getRecQty()) == 0 && DmShared.vNull(detail_Mdl.getChallanQty()) != 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Received Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (Math.abs(DmShared.vNull(detail_Mdl.getRecQty())) > Math.abs(DmShared.vNull(detail_Mdl.getChallanQty())) && (DmShared.vNull(detail_Mdl.getChallanQty()) > 0)) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Received Qty. should be less than or equal to Challan Qty in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (Math.abs(DmShared.vNull(detail_Mdl.getRejQty())) > Math.abs(DmShared.vNull(detail_Mdl.getRecQty()))) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Rejected Qty. should be less than or equal to Received Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getFreeQty()) == 0 && DmShared.vNull(detail_Mdl.getAcceptQty()) == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Accept Qty. or Free Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if ((DmShared.iNull(detail_Mdl.getIsReturn()).intValue() == 1 && DmShared.vNull(detail_Mdl.getSkuQty()) == 0)
                        || (DmShared.iNull(detail_Mdl.getIsReturn()).intValue() != 1 && DmShared.vNull(detail_Mdl.getSkuQty()) <= 0)) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter SKU Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getAcceptQty()) != 0 && DmShared.vNull(detail_Mdl.getRate()) == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getAcceptQty()) != 0 && DmShared.vNull(detail_Mdl.getAmount()) == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Amount in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase("")) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Batch No. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.iNull(detail_Mdl.getIsBarcodeActive()).intValue() == 1) {
                    if (DmShared.xNull(detail_Mdl.getBarcode().trim()).equalsIgnoreCase("")) {
                        throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Barcode is mandatory in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
                if (DmShared.vNull(detail_Mdl.getMrp()) == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter MRP in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getMrp()) < DmShared.vNull(detail_Mdl.getRate())) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "MRP should be greater than or equal to rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getSaleRate()) == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Sale Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getSaleRate()) > DmShared.vNull(detail_Mdl.getMrp())) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Sale Rate should be less than or equal to MRP in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.xNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase("")) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Expiry Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.iNull(mrTemp.getDefaultValueMdl().getIsPbillMrpLessthenPo()).intValue() == 0) {
                    if (!DmShared.xNull(detail_Mdl.getPoDocId()).equalsIgnoreCase("")) {
                        if (DmShared.vNull(detail_Mdl.getMrp()) < DmShared.vNull(detail_Mdl.getPodMrp())) {
                            throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "MRP Can't be less than to order MRP" + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    }
                }
            }
        }
        mrTemp.setLineItemCodesCsv(lineItemCodesCsv);
        mrTemp.setPoItemChangeCondition(poItemChangeCondition);

        if (!flag) {
            throw new DmException(700, "Medicines are mandatory.");
        }

        if (mrTemp.getDefaultValueMdl() != null) {

            if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Site's State in Site Master");
            }

            if (DmShared.xNull(mrTemp.getSupplierStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Supplier's State in Supplier Master");
            }

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED)
                    && !DmShared.xNull(mrTemp.getPartyTypeForEntry()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                throw new DmException(0, "Only unregistered suppliers are allowed for " + DmShared.xNull(mrTemp.getVoucherTypeName()));
            }

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_BILL_DIRECT)
                    && DmShared.xNull(mrTemp.getPartyTypeForEntry()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                throw new DmException(0, "Unregistered suppliers are not allowed for " + DmShared.xNull(mrTemp.getVoucherTypeName()));
            }

            if (!DmShared.dNull(mrTemp.getDefaultValueMdl().getSiteGstinDate()).trim().equals("")
                    && !DmShared.dNull(mrTemp.getSupplierBillDate()).trim().equals("")
                    && DmShared.convertDate(mrTemp.getDefaultValueMdl().getSiteGstinDate()).getTime() <= DmShared.convertDate(mrTemp.getSupplierBillDate()).getTime()
                    && DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteRegistrationType()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {

                if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteGstinDate()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Please fill Site's GSTN No. & GSTIN Date in Site Master");
                }

                if (DmShared.xNull(mrTemp.getPartyType()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Please fill Supplier's Registration Type in Supplier Master");
                }

                if (DmShared.xNull(mrTemp.getPartyTypeForEntry()).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Please fill Supplier's GSTN No. & GSTIN Date in Supplier Master");
                }
            }
        } else {
            throw new DmException(0, "Default values not available!");
        }

    }

}
