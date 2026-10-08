package aarogya.pharmacy.purchaseReturn.shared;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

import java.util.List;

public class PurchaseReturn_Vld {

    public static void chkSave(PurchaseReturn_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode().trim(), "Return Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Return Date", 200);
        DmShared.isValidText(mrTemp.getSupplierCode(), "Supplier", 300);
        DmShared.isValidText(mrTemp.getDepartmentCode(), "Department", 400);
        DmShared.isValidText(mrTemp.getGstReasonCode(), "Reason", 500);
        DmShared.isValidText(mrTemp.getProformaBillNo(), "Proforma No.", 600);

        if (DmShared.vNull(mrTemp.getNetAmount()).doubleValue() < 0) {
            throw new DmException(0, "Net Amount can't be less than zero.");
        }

        boolean flag = false;
        String pbItemChangeCondition = "";
        List< GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                flag = true;

                /*==============================<< Create consition string for pBill item change check >>===============================*/
                if (!pbItemChangeCondition.trim().equalsIgnoreCase("")) {
                    pbItemChangeCondition += " OR ";
                } else {
                    pbItemChangeCondition += " WHERE ";
                }
                pbItemChangeCondition += " (pbd.docId = '" + DmShared.xNull(detail_Mdl.getPbDocId()) + "'";
                pbItemChangeCondition += " AND pbd.v_SNo = '" + DmShared.xNull(detail_Mdl.getPbV_SNo()) + "'";
                pbItemChangeCondition += " AND pbd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                if (DmShared.vNull(detail_Mdl.getSharedKeyFieldCode()) == 0) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Medicine in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getPurchaseUnitCode()) == 0) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Unit in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getQuantity()) == 0 && DmShared.vNull(detail_Mdl.getFreeQty()) == 0) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Quantity or Free Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getSkuQty()) <= 0) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter SKU Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getQuantity()) != 0 && DmShared.vNull(detail_Mdl.getRate()) == 0) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.vNull(detail_Mdl.getQuantity()) != 0 && DmShared.vNull(detail_Mdl.getAmount()) == 0) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Amount in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.xNull(detail_Mdl.getBatchNo()).trim().equals("")) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Batch No. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.dNull(detail_Mdl.getExpiryDate()).trim().equals("")) {
                    throw new DmException(800, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Expiry Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

            }
        }
        mrTemp.setPbItemChangeCondition(pbItemChangeCondition);
        if (!flag) {
            throw new DmException(800, "Medicines are mandatory.");
        }

        if (mrTemp.getDefaultValueMdl() != null) {

            if (DmShared.xNull(mrTemp.getDefaultValueMdl().getSiteStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Site's State in Site Master");
            }

            if (DmShared.xNull(mrTemp.getSupplierStateCode()).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Please fill Supplier's State in Supplier Master");
            }

            if (!DmShared.dNull(mrTemp.getDefaultValueMdl().getSiteGstinDate()).trim().equals("")
                    && !DmShared.dNull(mrTemp.getVoucherDate()).trim().equals("")
                    && DmShared.convertDate(mrTemp.getDefaultValueMdl().getSiteGstinDate()).getTime() <= DmShared.convertDate(mrTemp.getVoucherDate()).getTime()
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
