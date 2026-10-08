package aarogya.inventory.enquiry.shared;

import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public class Enquiry_Vld {

    public static void chkSave(Enquiry_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getEnquiryTypeCode().trim(), "Enquiry Type", 100);
        DmShared.isValidText(mrTemp.getEnquiryDt().trim(), "Enquiry Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag().trim(), "Department", 500);

        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getGrdItem_Mdl();
        boolean flagItems = false;
        String indentV_DateChangeCondition = "", indentApprovalChangeCondition = "", indentItemChangeCondition = "";
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flagItems = true;
                if (!DmShared.dNull(detail_Mdl.getScheduleDt()).equalsIgnoreCase("")) {
                    if (!DmShared.dNull(detail_Mdl.getScheduleDt()).equalsIgnoreCase("")) {
                        if (DmShared.convertDate(DmClient.returnDate(detail_Mdl.getScheduleDt())).getTime() < DmShared.convertDate(mrTemp.getEnquiryDt()).getTime()) {
                            throw new DmException(300, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Required Date should be greater than Enquiry Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    }
                }
                if (!DmShared.xNull(detail_Mdl.getPropertyList()).trim().equalsIgnoreCase("")) {
                    ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();
                    propertyPopup_Mdl.setColourCode(detail_Mdl.getColourCode());
                    propertyPopup_Mdl.setColourName(detail_Mdl.getColourName());
                    propertyPopup_Mdl.setSizeCode(detail_Mdl.getSizeCode());
                    propertyPopup_Mdl.setSizeName(detail_Mdl.getSizeName());
                    propertyPopup_Mdl.setItemMakeCode(detail_Mdl.getItemMakeCode());
                    propertyPopup_Mdl.setItemMakeName(detail_Mdl.getItemMakeName());
                    try {
                        ItemPropertyPopup_Vld.validateProperties(getApplicableProperties(detail_Mdl.getPropertyList(), detail_Mdl.getSharedKeyFieldCode()), "0", mrTemp.getEnquiryDt(), propertyPopup_Mdl);
                    } catch (DmException e) {
                        throw new DmException(300, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please fill all properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
            }

            String previousIndDocId = "";
            for (GrdIndentDetails_Mdl grdIndentDetail : detail_Mdl.getGrdIndentDetail()) {
                /*==============================<< Create condition string for indent Voucher Date change check >>===============================*/
                if (!indentV_DateChangeCondition.trim().equalsIgnoreCase("")) {
                    indentV_DateChangeCondition += " OR ";
                } else {
                    indentV_DateChangeCondition += " WHERE ";
                }
                if (!previousIndDocId.equalsIgnoreCase(DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()))) {
                    indentV_DateChangeCondition += " (ind.docId = '" + DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()) + "')";
                }

                /*==============================<< Create condition string for indent approval change check >>===============================*/
                if (!indentApprovalChangeCondition.trim().equalsIgnoreCase("")) {
                    indentApprovalChangeCondition += " OR ";
                } else {
                    indentApprovalChangeCondition += " WHERE ";
                }
                if (!previousIndDocId.equalsIgnoreCase(DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()))) {
                    indentApprovalChangeCondition += " (ind.docId = '" + DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()) + "' and ISNULL(ind.approvedBy,'')='' )";
                }

                /*==============================<< Create condition string for indent item change check >>===============================*/
                if (!indentItemChangeCondition.trim().equalsIgnoreCase("")) {
                    indentItemChangeCondition += " OR ";
                } else {
                    indentItemChangeCondition += " WHERE ";
                }
                if (!previousIndDocId.equalsIgnoreCase(DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()))) {
                    indentItemChangeCondition += " (ind.docId = '" + DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()) + "'";
                    indentItemChangeCondition += " AND ind.v_SNo = '" + DmShared.xNull(grdIndentDetail.getIndentVSNo()) + "'";
                    indentItemChangeCondition += " AND ind.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";
                }

                previousIndDocId = DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode());

                /*==============================<< other validations >>===============================*/
                if (DmShared.convertDate(DmClient.returnDate(grdIndentDetail.getIndentDate())).getTime() > DmShared.convertDate(mrTemp.getEnquiryDt()).getTime()) {
                    throw new DmException(300, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Indent Date should be less than Enquiry Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
            }

        }

        mrTemp.setIndentV_DateChangeCondition(indentV_DateChangeCondition);
        mrTemp.setIndApprovalChangeCondition(indentApprovalChangeCondition);
        mrTemp.setIndItemChangeCondition(indentItemChangeCondition);
        if (!flagItems) {
            throw new DmException(300, "Item Details is mandatory.");
        }

        List<GrdVendor_Mdl> listSupplierDetail = mrTemp.getGrdVen_Mdl();
        boolean flagSupplier = false;
        for (GrdVendor_Mdl detail_Mdl : listSupplierDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flagSupplier = true;
            }
        }
        if (!flagSupplier) {
            throw new DmException(400, "Suppliers are mandatory.");
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
