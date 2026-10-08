package aarogya.inventory.workOrder.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class WorkOrder_Vld {

    public static void chkSave(WorkOrder_Mdl mrTemp, String currentDate) {
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
        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getGrdItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
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
                if (!DmShared.xNull(detail_Mdl.getItemProperty()).trim().equalsIgnoreCase("")) {
                    ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();
                    propertyPopup_Mdl.setItemCode(detail_Mdl.getSharedKeyFieldCode());
                    propertyPopup_Mdl.setColourCode(detail_Mdl.getColorCode());
                    propertyPopup_Mdl.setColourName(detail_Mdl.getColor());
                    propertyPopup_Mdl.setSizeCode(detail_Mdl.getSizeCode());
                    propertyPopup_Mdl.setSizeName(detail_Mdl.getSize());
                    try {
                        ItemPropertyPopup_Vld.validateProperties(detail_Mdl.getProperties(), detail_Mdl.getIsBestBefore(), mrTemp.getVoucherDate(), propertyPopup_Mdl);
                    } catch (DmException e) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please fill all properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }

                if (DmShared.vNull(detail_Mdl.getOrderQuantity()).doubleValue() == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (DmShared.vNull(detail_Mdl.getRate()).doubleValue() == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Rate in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (DmShared.vNull(detail_Mdl.getSkuQty()).doubleValue() == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter SKU Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (DmShared.vNull(detail_Mdl.getAmount()).doubleValue() == 0) {
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
            throw new DmException(500, "Items are mandatory.");
        }
    }

    public static String getPOApplicableProperties(String propertyList) {
        String poApplicableProperties = "";
        for (String property : DmShared.xNull(propertyList).split(",")) {
            if (DmShared.xNull(property).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())) {
                if (!DmShared.xNull(poApplicableProperties).equalsIgnoreCase("")) {
                    poApplicableProperties += ",";
                }
                poApplicableProperties += property;
            } else if (DmShared.xNull(property).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())) {
                if (!DmShared.xNull(poApplicableProperties).equalsIgnoreCase("")) {
                    poApplicableProperties += ",";
                }
                poApplicableProperties += property;
            }
        }

        return poApplicableProperties;
    }

}
