package aarogya.inventory.requisition.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class Requisition_Vld {

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

    public static void chkSave(Requisition_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getRequisitionTypeCode().trim(), "Requisition Type", 100);
        DmShared.isValidText(mrTemp.getDate().trim(), "Requisition Date", 200);
        DmShared.isValidText(mrTemp.getSubCode().trim(), "Requisition By", 300);
        DmShared.isValidText(mrTemp.getDepartmentByCode().trim(), "Department By", 400);
        DmShared.isValidText(mrTemp.getDepartmentToCode().trim(), "Department To", 500);
        if (DmShared.xNull(mrTemp.getDepartmentByCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getDepartmentToCode()))) {
            throw new DmException(400, "Department to and Department by cannot be same.");
        }
        boolean flag = false;
        List<GrdRequisition_Mdl> listItemDetail = mrTemp.getGrdData();
        for (GrdRequisition_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (!DmShared.dNull(detail_Mdl.getDate()).trim().equals("")) {
                    if (DmShared.convertDate(detail_Mdl.getDate()).getTime() < DmShared.convertDate(mrTemp.getDate()).getTime()) {
                        throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Req. Date should be greater than or equal to Requisition Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
            }
        }
        if (!flag) {
            throw new DmException(600, "Requisition Detail is mandatory.");
        }

    }
}
