package aarogya.pharmacy.quotation.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class Quotation_Vld {

    public static void chkSave(Quotation_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Quotation Type", 100);
        DmShared.isValidText(mrTemp.getVendorTag(), "Supplier Name", 200);
        DmShared.isValidText(mrTemp.getQuotationDate(), "Quotation Date", 300);
        DmShared.isValidText(mrTemp.getVendorRefNo(), "Supplier Ref. No.", 400);
        DmShared.isValidText(mrTemp.getVendorRefDate(), "Supplier Ref. Date.", 500);

        if (!DmShared.dNull(mrTemp.getVendorRefDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getQuotationDate()).getTime() < DmShared.convertDate(mrTemp.getVendorRefDate()).getTime()) {
                throw new DmException(300, "Quotation Date should be greater than or equal to Supplier Ref. Date");
            }
        }
        if (!DmShared.dNull(mrTemp.getEnquiryRefDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getQuotationDate()).getTime() < DmShared.convertDate(mrTemp.getEnquiryRefDate()).getTime()) {
                throw new DmException(300, "Quotation Date should be greater than or equal to Enquiry Ref. Date");
            }
        }

        boolean flag = false;
        String indentV_DateChangeCondition = "", enqItemChangeCondition = "", indentApprovalChangeCondition = "", indentItemChangeCondition = "";
        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;

                /*==============================<< Create condition string for ENQUIRY item change check >>===============================*/
                if (!enqItemChangeCondition.trim().equalsIgnoreCase("")) {
                    enqItemChangeCondition += " OR ";
                } else {
                    enqItemChangeCondition += " WHERE ";
                }
                enqItemChangeCondition += " (enq.docId = '" + DmShared.xNull(detail_Mdl.getEnquiryDocId()) + "'";
                enqItemChangeCondition += " AND enq.v_SNo = '" + DmShared.xNull(detail_Mdl.getEnquiryV_SNo()) + "'";
                enqItemChangeCondition += " AND enq.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                /*==============================<< other validations >>===============================*/
                if (!DmShared.dNull(detail_Mdl.getDeliveryDate()).equalsIgnoreCase("")) {
                    if ((DmShared.convertDate(detail_Mdl.getDeliveryDate()).getTime() < (DmShared.convertDate(mrTemp.getQuotationDate())).getTime())) {
                        throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Delivery Date should be greater than or equal to Quotation Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }

                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                    throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (DmShared.vNull(detail_Mdl.getRate()).doubleValue() == 0) {
                    throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                List<GrdIndentDetail_Mdl> listIndentDetail = detail_Mdl.getListIndentDetail();
                String previousIndDocId = "";
                for (GrdIndentDetail_Mdl indDetail_Mdl : listIndentDetail) {
                    /*==============================<< Create condition string for indent Voucher Date change check >>===============================*/
                    if (!indentV_DateChangeCondition.trim().equalsIgnoreCase("")) {
                        indentV_DateChangeCondition += " OR ";
                    } else {
                        indentV_DateChangeCondition += " WHERE ";
                    }
                    if (!previousIndDocId.equalsIgnoreCase(DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode()))) {
                        indentV_DateChangeCondition += " (ind.docId = '" + DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode()) + "')";
                    }
                    /*==============================<< Create condition string for indent approval change check >>===============================*/
                    if (!indentApprovalChangeCondition.trim().equalsIgnoreCase("")) {
                        indentApprovalChangeCondition += " OR ";
                    } else {
                        indentApprovalChangeCondition += " WHERE ";
                    }
                    if (!previousIndDocId.equalsIgnoreCase(DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode()))) {
                        indentApprovalChangeCondition += " (ind.docId = '" + DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode()) + "' and ISNULL(ind.approvedBy,'')='' )";
                    }

                    /*==============================<< Create condition string for indent item change check >>===============================*/
                    if (!indentItemChangeCondition.trim().equalsIgnoreCase("")) {
                        indentItemChangeCondition += " OR ";
                    } else {
                        indentItemChangeCondition += " WHERE ";
                    }
                    if (!previousIndDocId.equalsIgnoreCase(DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode()))) {
                        indentItemChangeCondition += " (ind.docId = '" + DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode()) + "'";
                        indentItemChangeCondition += " AND ind.v_SNo = '" + DmShared.xNull(indDetail_Mdl.getIndentVSNo()) + "'";
                        indentItemChangeCondition += " AND ind.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";
                    }

                    previousIndDocId = DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode());

                    if (!DmShared.xNull(indDetail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        if (!DmShared.dNull(indDetail_Mdl.getIndentDate()).equalsIgnoreCase("")) {
                            if ((DmShared.convertDate(indDetail_Mdl.getIndentDate()).getTime() > (DmShared.convertDate(mrTemp.getQuotationDate())).getTime())) {
                                throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Indent Date cannot be greater than Quotation Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                            if (!DmShared.dNull(mrTemp.getVendorRefDate()).equalsIgnoreCase("")) {
                                if ((DmShared.convertDate(indDetail_Mdl.getIndentDate()).getTime() > (DmShared.convertDate(mrTemp.getVendorRefDate())).getTime())) {
                                    throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Indent Date cannot be greater than Reference Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                                }
                            }
                        }
                    }
                }
            }
        }

        mrTemp.setIndentV_DateChangeCondition(indentV_DateChangeCondition);
        mrTemp.setEnqItemChangeCondition(enqItemChangeCondition);
        mrTemp.setIndApprovalChangeCondition(indentApprovalChangeCondition);
        mrTemp.setIndItemChangeCondition(indentItemChangeCondition);
        if (!flag) {
            throw new DmException(600, "Medicine details are mandatory.");
        }
    }
}
