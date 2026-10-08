package aarogya.inventory.itemOpening.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public class ItemOpening_Vld {

    public static void chkSave(ItemOpening_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Opening Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Opening Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Godown In", 300);

        boolean flag = false;
        ItemProperty_Mdl propertyPopup_Mdl;

        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                flag = true;
                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                    throw new DmException(400, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. for in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if (mrTemp.isInventoryAccountingActive()) {
                    if (DmShared.iNull(detail_Mdl.getIsubCode()).longValue() == 0) {
                        throw new DmException(400, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Inventory a/c is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }

                //<editor-fold defaultstate="collapsed" desc="Properties check">
                if (!DmShared.xNull(detail_Mdl.getPropertyList()).trim().equals("")) {
                    propertyPopup_Mdl = new ItemProperty_Mdl();
                    propertyPopup_Mdl.setItemCode(detail_Mdl.getSharedKeyFieldCode());
                    propertyPopup_Mdl.setBatchNo(detail_Mdl.getBatchNo());
                    propertyPopup_Mdl.setLotNo(detail_Mdl.getLotNo());
                    propertyPopup_Mdl.setManufacturingDate(detail_Mdl.getManufacturingDate());
                    propertyPopup_Mdl.setExpiryDate(detail_Mdl.getExpiryDate());
                    propertyPopup_Mdl.setMrp(detail_Mdl.getMrp());
                    propertyPopup_Mdl.setSkuMrp(detail_Mdl.getSkuMrp());
                    propertyPopup_Mdl.setSkuQtyForRate(detail_Mdl.getSkuQtyForRate());
                    propertyPopup_Mdl.setSkuName(detail_Mdl.getSkuName());
                    propertyPopup_Mdl.setColourCode(detail_Mdl.getColourCode());
                    propertyPopup_Mdl.setColourName(detail_Mdl.getColourName());
                    propertyPopup_Mdl.setSizeCode(detail_Mdl.getSizeCode());
                    propertyPopup_Mdl.setSizeName(detail_Mdl.getSizeName());
                    propertyPopup_Mdl.setBestBeforeDays(detail_Mdl.getBestBeforeDays());
                    propertyPopup_Mdl.setItemMakeCode(detail_Mdl.getItemMakeCode());
                    propertyPopup_Mdl.setItemMakeName(detail_Mdl.getItemMakeName());

                    try {
                        ItemPropertyPopup_Vld.validateProperties(detail_Mdl.getPropertyList(), detail_Mdl.getIsBestBefore(), mrTemp.getVoucherDate(), propertyPopup_Mdl);
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
