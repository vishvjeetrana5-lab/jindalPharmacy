package aarogya.pharmacy.purchaseOrder.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class PurchaseOrder_Vld {

    public static void chkSave(PurchaseOrder_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(mrTemp.getVoucherTypeTag().trim(), "PO Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "PO Date", 200);
        DmShared.isValidText(mrTemp.getSupplierTag(), "Supplier", 300);

        if (!DmShared.dNull(mrTemp.getVoucherDate()).equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() > (DmShared.convertDate(DmShared.dNull(currentDate)).getTime()))) {
                throw new DmException(200, "PO Date should be less than or equal to Current Date.");
            }
        }
        if (!DmShared.dNull(mrTemp.getRefDate()).equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(DmShared.dNull(mrTemp.getRefDate())).getTime()))) {
                throw new DmException(200, "PO Date should be greater than or equal to Ref. Date.");
            }
        }
        if (!DmShared.dNull(mrTemp.getQuotationDate()).equalsIgnoreCase("")) {
            if (!DmShared.dNull(mrTemp.getRefDate()).trim().equals("")) {
                if (DmShared.convertDate(mrTemp.getRefDate()).getTime() < DmShared.convertDate(mrTemp.getQuotationDate()).getTime()) {
                    throw new DmException(400, "Quotation Date should be less than or equal to Ref. Date.");
                }
            } else {
                if (DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < DmShared.convertDate(mrTemp.getQuotationDate()).getTime()) {
                    throw new DmException(400, "Quotation Date should be less than or equal to PO Date.");
                }
            }
        }

        boolean flag = false;
        String quotationItemChangeCondition = "", indentItemChangeCondition = "";
        List<GrdMedicineDetail_Mdl> listItemDetail = mrTemp.getGrdItemDetail();
        for (GrdMedicineDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                /*==============================<< Create condition string for PO item change check >>===============================*/
                if (!quotationItemChangeCondition.trim().equalsIgnoreCase("")) {
                    quotationItemChangeCondition += " OR ";
                } else {
                    quotationItemChangeCondition += " WHERE ";
                }
                quotationItemChangeCondition += " (qd.docId = '" + DmShared.xNull(detail_Mdl.getQuotationDocId()) + "'";
                quotationItemChangeCondition += " AND qd.v_SNo = '" + DmShared.xNull(detail_Mdl.getQuotationV_SNo()) + "'";
                quotationItemChangeCondition += " AND qd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                /*==============================<< Grid lines Validation >>===============================*/
                if (DmShared.vNull(detail_Mdl.getOrderQuantity()) == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (DmShared.vNull(detail_Mdl.getRate()) == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (DmShared.vNull(detail_Mdl.getSkuQty()) == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter SKU Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (DmShared.vNull(detail_Mdl.getAmount()) == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Amount in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (!DmShared.dNull(detail_Mdl.getReqDate()).equalsIgnoreCase("")) {
                    if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() > (DmShared.convertDate(DmShared.dNull(detail_Mdl.getReqDate())).getTime()))) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Required Date should be greater than or equal to PO Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }

                String previousIndDocId = "";
                for (GrdIndentDetail_Mdl iMdl : detail_Mdl.getListIndentDetail()) {

                    /*==============================<< Create condition string for indent item change check >>===============================*/
                    if (!indentItemChangeCondition.trim().equalsIgnoreCase("")) {
                        indentItemChangeCondition += " OR ";
                    } else {
                        indentItemChangeCondition += " WHERE ";
                    }
                    if (!previousIndDocId.equalsIgnoreCase(DmShared.xNull(iMdl.getSharedKeyFieldCode()))) {
                        indentItemChangeCondition += " (ind.docId = '" + DmShared.xNull(iMdl.getSharedKeyFieldCode()) + "'";
                        indentItemChangeCondition += " AND ind.v_SNo = '" + DmShared.xNull(iMdl.getIndentVSNo()) + "'";
                        indentItemChangeCondition += " AND ind.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";
                    }

                    previousIndDocId = DmShared.xNull(iMdl.getSharedKeyFieldCode());

                    /*==============================<< other validations >>===============================*/
                    if (!DmShared.dNull(mrTemp.getRefDate()).trim().equals("")) {
                        if (DmShared.convertDate(mrTemp.getRefDate()).getTime() < DmShared.convertDate(iMdl.getIndentDate()).getTime()) {
                            throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Indent Date should be less than or equal to Ref. Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    } else {
                        if (DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < DmShared.convertDate(iMdl.getIndentDate()).getTime()) {
                            throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Indent Date should be less than or equal to PO Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    }
                }
            }
        }
        mrTemp.setQuotationItemChangeCondition(quotationItemChangeCondition);
        mrTemp.setIndItemChangeCondition(indentItemChangeCondition);
        if (!flag) {
            throw new DmException(500, "Medicines are mandatory.");
        }
    }

}
