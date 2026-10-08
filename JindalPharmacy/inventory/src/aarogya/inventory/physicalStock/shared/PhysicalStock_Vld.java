package aarogya.inventory.physicalStock.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class PhysicalStock_Vld {

    public static void chkSave(PhysicalStock_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Issue Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Issue Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentCode(), "Godown ", 300);

        boolean flag = false;
        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        ItemProperty_Mdl propertyPopup_Mdl;

        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                    throw new DmException(400, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.iNull(detail_Mdl.getIbCode()).longValue() == 0) {
                    throw new DmException(400, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please re-select the item to refresh its properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                //<editor-fold defaultstate="collapsed" desc="Properties check">
                if (!DmShared.xNull(detail_Mdl.getPropertyList()).trim().equals("")) {
                    propertyPopup_Mdl = new ItemProperty_Mdl();
                    propertyPopup_Mdl.setItemCode(detail_Mdl.getSharedKeyFieldCode());
                    propertyPopup_Mdl.setBatchNo(detail_Mdl.getBatchNo());
                    propertyPopup_Mdl.setManufacturingDate(detail_Mdl.getManufacturingDate());
                    propertyPopup_Mdl.setExpiryDate(detail_Mdl.getExpiryDate());
                    propertyPopup_Mdl.setMrp(detail_Mdl.getMrp());
                    propertyPopup_Mdl.setSkuMrp(detail_Mdl.getSkuMrp());
                    propertyPopup_Mdl.setSkuQtyForRate(detail_Mdl.getPurchaseUnitConverionRate());
                    propertyPopup_Mdl.setSkuName(detail_Mdl.getSkuName());
                    propertyPopup_Mdl.setColourCode(detail_Mdl.getColourCode());
                    propertyPopup_Mdl.setColourName(detail_Mdl.getColourName());
                    propertyPopup_Mdl.setSizeCode(detail_Mdl.getSizeCode());
                    propertyPopup_Mdl.setSizeName(detail_Mdl.getSizeName());
                    propertyPopup_Mdl.setItemMakeCode(detail_Mdl.getItemMakeCode());
                    propertyPopup_Mdl.setItemMakeName(detail_Mdl.getItemMakeName());
                    propertyPopup_Mdl.setBestBeforeDays(detail_Mdl.getBestBeforeDays());
                    try {
                        ItemPropertyPopup_Vld.validateProperties(detail_Mdl.getPropertyList(), detail_Mdl.getIsBestBefore(), mrTemp.getVoucherDate(), propertyPopup_Mdl);
                        if (DmShared.vNull(detail_Mdl.getIbCode()).doubleValue() == 0) {
                            throw new DmException(400, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "IbCode required in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    } catch (DmException e) {
                        throw new DmException(400, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please fill all properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
                //</editor-fold>
            }
        }

        if (!flag) {
            throw new DmException(400, "Items are mandatory.");
        }
    }

}
