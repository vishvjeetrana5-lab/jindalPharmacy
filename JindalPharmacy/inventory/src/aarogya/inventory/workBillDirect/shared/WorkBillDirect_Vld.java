package aarogya.inventory.workBillDirect.shared;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class WorkBillDirect_Vld {

    private static ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    public static void chkSave(WorkBillDirect_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode().trim(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 200);
        DmShared.isValidText(mrTemp.getSupplierTag(), "Supplier", 300);
        DmShared.isValidText(mrTemp.getSupplierBillNo(), "Ref. No.", 400);
        DmShared.isValidText(mrTemp.getSupplierBillDate(), "Ref. Date", 500);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department", 600);
        DmShared.isValidText(mrTemp.getCalculateOnTag(), "Calc. On", 650);

        if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(mrTemp.getSupplierBillDate()).getTime()))) {
            throw new DmException(500, "Voucher Date should be greater than or equal to Ref. Date.");
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
                if (DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase("")) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Unit in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (!DmShared.xNull(detail_Mdl.getPropertyList()).trim().equalsIgnoreCase("")) {
                    ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();
                    propertyPopup_Mdl.setItemCode(detail_Mdl.getSharedKeyFieldCode());
                    propertyPopup_Mdl.setBatchNo(detail_Mdl.getBatchNo());
                    propertyPopup_Mdl.setManufacturingDate(detail_Mdl.getManufacturingDate());
                    propertyPopup_Mdl.setExpiryDate(detail_Mdl.getExpiryDate());
                    propertyPopup_Mdl.setMrp(detail_Mdl.getMrp());
                    propertyPopup_Mdl.setSkuMrp(detail_Mdl.getSkuMrp());
                    propertyPopup_Mdl.setColourCode(detail_Mdl.getColourCode());
                    propertyPopup_Mdl.setColourName(detail_Mdl.getColourName());
                    propertyPopup_Mdl.setSizeCode(detail_Mdl.getSizeCode());
                    propertyPopup_Mdl.setSizeName(detail_Mdl.getSizeName());
                    try {
                        ItemPropertyPopup_Vld.validateProperties(detail_Mdl.getPropertyList(), detail_Mdl.getIsBestBefore(), mrTemp.getVoucherDate(), propertyPopup_Mdl);
                    } catch (DmException e) {
                        throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please fill all properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
                if (DmShared.vNull(detail_Mdl.getChallanQty()).doubleValue() == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Challan Quantity in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getRecQty()).doubleValue() == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Received Quantity in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getAcceptQty()).doubleValue() == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Accept Quantity in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getRate()).doubleValue() == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getAmount()).doubleValue() == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Amount in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getSkuQty()).doubleValue() == 0) {
                    throw new DmException(700, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter SKU Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
            }
        }
        mrTemp.setLineItemCodesCsv(lineItemCodesCsv);
        mrTemp.setPoItemChangeCondition(poItemChangeCondition);

        if (!flag) {
            throw new DmException(700, "Items are mandatory.");
        }

        if (mrTemp.getDefaultValueMdl() != null) {

            if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Site's State in Site Master");
            }

            if (DmShared.xNull(mrTemp.getSupplierStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Supplier's State in Supplier Master");
            }

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PURCHASE_BILL_DIRECT_UNREGISTERED)
                    && !DmShared.xNull(mrTemp.getPartyTypeForEntry()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                throw new DmException(0, "Only unregistered suppliers are allowed for " + DmShared.xNull(mrTemp.getVoucherTypeName()));
            }

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PURCHASE_BILL_DIRECT)
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
