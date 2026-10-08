package aarogya.inventory.indent.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class Indent_Vld {

    public static void chkSave(Indent_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getIndentTypeTag(), "Indent Type", 100);
        DmShared.isValidText(mrTemp.getDtIndent(), "Indent Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department", 300);
        DmShared.isValidText(mrTemp.getIndentorTag(), "Indentor", 400);

        boolean flag = false;
        String lineitemCodesCsv = "";
        String reqItemChangeCondition = "";
        String reqQtyChangeCondition = "";
        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getGrdItemDetails();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                flag = true;

                /*==============================<< Create CSV for restricted department check >>===============================*/
                if (!lineitemCodesCsv.trim().equalsIgnoreCase("")) {
                    lineitemCodesCsv += ",";
                }
                lineitemCodesCsv += DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());

                if (!DmShared.xNull(detail_Mdl.getRequisitionDocId()).trim().equals("")) {

                    /*==============================<< Create condition string for REQUISITION item change check >>===============================*/
                    if (!reqItemChangeCondition.trim().equalsIgnoreCase("")) {
                        reqItemChangeCondition += " OR ";
                    } else {
                        reqItemChangeCondition = " WHERE ";
                    }
                    reqItemChangeCondition += " (rqd.docId = '" + DmShared.xNull(detail_Mdl.getRequisitionDocId()) + "'";
                    reqItemChangeCondition += " AND rqd.v_SNo = '" + DmShared.xNull(detail_Mdl.getRequisitionVsNo()) + "'";
                    reqItemChangeCondition += " AND rqd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                    /*==============================<< Create condition string for REQUISITION Qty change check >>===============================*/
                    if (!reqQtyChangeCondition.trim().equalsIgnoreCase("")) {
                        reqQtyChangeCondition += " OR ";
                    } else {
                        reqQtyChangeCondition = " WHERE ";
                    }
                    reqQtyChangeCondition += " (rqd.docId = '" + DmShared.xNull(detail_Mdl.getRequisitionDocId()) + "'";
                    reqQtyChangeCondition += " AND rqd.v_SNo = '" + DmShared.xNull(detail_Mdl.getRequisitionVsNo()) + "'";
                    reqQtyChangeCondition += " AND (ISNULL(rqd.reqQty,0) - ISNULL(rqd.issueQty,0)) != " + DmShared.vNull(detail_Mdl.getQty().toString()) + ")";

                }

                /*==============================<< other validations >>===============================*/
                if ((!DmShared.dNull(mrTemp.getDtIndent()).equalsIgnoreCase("")) && (!DmShared.dNull(detail_Mdl.getReqDate()).equalsIgnoreCase(""))) {
                    if (DmShared.convertDate(detail_Mdl.getReqDate()).getTime() < DmShared.convertDate(mrTemp.getDtIndent()).getTime()) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Require Date should be greater than Indent Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
                if (!DmShared.dNull(detail_Mdl.getRequisitionDate()).equalsIgnoreCase("")) {
                    if (DmShared.convertDate(detail_Mdl.getRequisitionDate()).getTime() > DmShared.convertDate(mrTemp.getDtIndent()).getTime()) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Requisition Date should be less than Indent Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
                if (!DmShared.xNull(detail_Mdl.getItemProperty()).trim().equalsIgnoreCase("")) {
                    ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();

                    propertyPopup_Mdl.setColourCode(detail_Mdl.getColorCode());
                    propertyPopup_Mdl.setColourName(detail_Mdl.getColor());
                    propertyPopup_Mdl.setSizeCode(detail_Mdl.getSizeCode());
                    propertyPopup_Mdl.setSizeName(detail_Mdl.getSize());
                    propertyPopup_Mdl.setItemMakeName(detail_Mdl.getItemMakeName());
                    propertyPopup_Mdl.setItemMakeCode(detail_Mdl.getItemMakeCode());

                    try {
                        ItemPropertyPopup_Vld.validateProperties(getApplicableProperties(detail_Mdl.getItemProperty(), detail_Mdl.getSharedKeyFieldCode()), "0", mrTemp.getDtIndent(), propertyPopup_Mdl);
                    } catch (DmException e) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please fill all properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
            }
        }

        mrTemp.setLineItemCodesCsv(lineitemCodesCsv);
        mrTemp.setReqItemChangeCondition(reqItemChangeCondition);
        mrTemp.setReqQtyChangeCondition(reqQtyChangeCondition);

        if (!flag) {
            throw new DmException(500, "Items are mandatory.");
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
