package aarogya.selfToolkit.itemPropertyPopup.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.ArrayList;

public class ItemPropertyPopup_Vld {

    private static String strMandatory = " is mandatory.";

    public static void validateProperties(String properties, String isBestBefore, String vDate, ItemProperty_Mdl ipm) {
        ArrayList<String> lstProperties = new ArrayList<>();

        String[] arrProperty = properties.split(",");
        for (String list : arrProperty) {
            lstProperties.add(list);
        }
        if (lstProperties.contains(ItemProperty_Mdl.Properties.BATCH_NO.getPropertyManualCode())) {
            if (DmShared.xNull(ipm.getBatchNo()).equalsIgnoreCase("")) {
                throw new DmException(100, "Batch No" + strMandatory);
            }
        }
        if (lstProperties.contains(ItemProperty_Mdl.Properties.EXPIRY_DATE.getPropertyManualCode())) {
            if (DmShared.iNull(isBestBefore).intValue() == 1) {
                if (DmShared.dNull(ipm.getManufacturingDate()).equalsIgnoreCase("")) {
                    throw new DmException(200, "Mfg. Date" + strMandatory);
                }
                if (DmShared.convertDate(ipm.getManufacturingDate()).getTime() > DmShared.convertDate(vDate).getTime()) {
                    throw new DmException(200, "Mfg. date should be less than or equal to current date.");
                }
//                if (DmShared.xNull(ipm.getBestBeforeDays()).equalsIgnoreCase("")) {
//                    throw new DmException(300, "Best Before Days" + strMandatory);
//                }
            }
            if (DmShared.dNull(ipm.getExpiryDate()).equalsIgnoreCase("")) {
                throw new DmException(400, "Expiry Date" + strMandatory);
            }
//            if (!ipm.isFromOpening()) {
            if (!ipm.isIsCheckExpiryDate()) {
                if (!DmShared.dNull(ipm.getExpiryDate()).equalsIgnoreCase("") && !DmShared.dNull(vDate).equalsIgnoreCase("")) {
                    if (DmShared.convertDate(ipm.getExpiryDate()).getTime() <= DmShared.convertDate(vDate).getTime()) {
                        throw new DmException(400, "Expiry date should be greater than entry date.");
                    }
                }
            }
        }
        if (lstProperties.contains(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())) {
            if (DmShared.xNull(ipm.getColourCode()).equalsIgnoreCase("")) {
                throw new DmException(500, "Colour" + strMandatory);
            }
        }
        if (lstProperties.contains(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())) {
            if (DmShared.xNull(ipm.getSizeCode()).equalsIgnoreCase("")) {
                throw new DmException(600, "Size" + strMandatory);
            }
        }
        if (lstProperties.contains(ItemProperty_Mdl.Properties.MRP.getPropertyManualCode())) {
            if (DmShared.vNull(ipm.getMrp()).doubleValue() <= 0) {
                throw new DmException(700, "MRP" + strMandatory);
            }
        }
    }
}
