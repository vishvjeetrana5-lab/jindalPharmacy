package aarogya.inventory.quotation.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public class Quotation_Vld {

    public static void chkSave(Quotation_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Quotation Type", 100);
        DmShared.isValidText(mrTemp.getVendorTag(), "Supplier Name", 200);
        DmShared.isValidText(mrTemp.getQuotationDate(), "Quotation Date", 300);
        DmShared.isValidText(mrTemp.getVendorRefNo(), "Supplier Ref. No.", 400);
        DmShared.isValidText(mrTemp.getVendorRefDate(), "Supplier Ref. Date", 500);

        if (!DmShared.dNull(mrTemp.getVendorRefDate()).trim().equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getQuotationDate()).getTime() < DmShared.convertDate(mrTemp.getVendorRefDate()).getTime()) {
                throw new DmException(300, "Quotation Date should be greater than or equal to Supplier Ref. Date");
            }
        }
        if (!DmShared.dNull(mrTemp.getEnquiryRefDate()).trim().equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getQuotationDate()).getTime() < DmShared.convertDate(mrTemp.getEnquiryRefDate()).getTime()) {
                throw new DmException(300, "Quotation Date should be greater than or equal to Enquiry Ref. Date");
            }
        }

        if (!DmShared.dNull(mrTemp.getValidUptoDate()).trim().equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getValidUptoDate()).getTime() < DmShared.convertDate(mrTemp.getQuotationDate()).getTime()) {
                throw new DmException(700, "Valid Upto should be greater than or equal to Quotation date.");
            }
        }

        if (!DmShared.dNull(mrTemp.getVendorRefDate()).trim().equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getVendorRefDate()).getTime() > DmShared.convertDate(mrTemp.getQuotationDate()).getTime()) {
                throw new DmException(700, "Supplier Ref. Date should be less than or equal to current date.");
            }
        }

        if (!DmShared.dNull(mrTemp.getEnquiryRefDate()).trim().equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getEnquiryRefDate()).getTime() > DmShared.convertDate(mrTemp.getQuotationDate()).getTime()) {
                throw new DmException(700, "Enquiry Ref. Date should be less than or equal to Quotation date.");
            }
        }

        boolean flag = false;
        String indentV_DateChangeCondition = "", enqItemChangeCondition = "", indentApprovalChangeCondition = "", indentItemChangeCondition = "";
        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                if (!detail_Mdl.isServiceRecord()) {
                    flag = true;

                    if (!DmShared.xNull(detail_Mdl.getPropertyList()).trim().equalsIgnoreCase("")) {
                        ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();
                        propertyPopup_Mdl.setColourCode(detail_Mdl.getColourCode());
                        propertyPopup_Mdl.setColourName(detail_Mdl.getColourName());
                        propertyPopup_Mdl.setSizeCode(detail_Mdl.getSizeCode());
                        propertyPopup_Mdl.setSizeName(detail_Mdl.getSizeName());
                        propertyPopup_Mdl.setItemMakeCode(detail_Mdl.getItemMakeCode());
                        propertyPopup_Mdl.setItemMakeName(detail_Mdl.getItemMakeName());

                        try {
                            ItemPropertyPopup_Vld.validateProperties(getApplicableProperties(detail_Mdl.getPropertyList(), detail_Mdl.getSharedKeyFieldCode()), "0", mrTemp.getQuotationDate(), propertyPopup_Mdl);
                        } catch (DmException e) {
                            throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please fill all properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    }

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

                        /*==============================<< other validations >>===============================*/
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
        }

        mrTemp.setIndentV_DateChangeCondition(indentV_DateChangeCondition);

        mrTemp.setEnqItemChangeCondition(enqItemChangeCondition);

        mrTemp.setIndApprovalChangeCondition(indentApprovalChangeCondition);

        mrTemp.setIndItemChangeCondition(indentItemChangeCondition);

        if (!flag) {
            throw new DmException(600, "Item details are mandatory. ");
        }
    }

    public static String getApplicableProperties(String propertyList, String itemCode) {
        boolean flagApplicable = false;
        String propCode = "", property = DmShared.xNull(propertyList);
        if (!DmShared.xNull(itemCode).equalsIgnoreCase("")) {
            for (String prop : DmShared.xNull(property).split(",")) {
                flagApplicable = false;
                if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.ITEM_MAKE.getPropertyManualCode())) {
                    flagApplicable = true;

                }

                if (flagApplicable) {
                    if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                        propCode += ",";
                    }
                    propCode += prop;
                }
            }

        }
        return propCode;
    }
}
