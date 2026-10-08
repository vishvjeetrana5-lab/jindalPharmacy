package dataman.utility.itemTaxStructure.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class ItemTaxStructure_Vld {

    public static void chkSave(ItemTaxStructure_Mdl mrTemp) {
        boolean isDataExists = false;
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "ITS Name", 100);
        if (mrTemp.getGrdItemTaxStructure().size() <= 0) {
            throw new DmException(200, "Atleast one ITS should be define.");
        }

        if (mrTemp.getNature().equalsIgnoreCase("")) {
            throw new DmException(200, "Nature is mendatory.");
        }
        if (mrTemp.getTotalTaxPercent().equalsIgnoreCase("")) {
            throw new DmException(200, "Total Tax Percent is mendatory");
        }

        String tag1, tag2, tag3, tag4;
        for (int i = 0; i < mrTemp.getGrdItemTaxStructure().size(); i++) {
            if (DmShared.iNull(mrTemp.getGrdItemTaxStructure().get(i).getSharedKeyFieldCode()).longValue() > 0) {
                GrdItemTaxStructure_Mdl voucherLink_Mdl = mrTemp.getGrdItemTaxStructure().get(i);
                tag1 = DmShared.xNull(voucherLink_Mdl.getLocationTag());
                tag2 = DmShared.xNull(voucherLink_Mdl.getSharedKeyFieldCode());
                tag3 = DmShared.xNull(voucherLink_Mdl.getSiteTag());
//                tag4 = DmShared.xNull(voucherLink_Mdl.getApplicableFrom());
                for (int j = i + 1; j < mrTemp.getGrdItemTaxStructure().size() - 1; j++) {
                    GrdItemTaxStructure_Mdl voucherLink_Mdl1 = mrTemp.getGrdItemTaxStructure().get(j);
                    if (DmShared.xNull(voucherLink_Mdl1.getLocationTag()).equalsIgnoreCase(tag1)) {
                        if (DmShared.xNull(voucherLink_Mdl1.getSharedKeyFieldCode()).equalsIgnoreCase(tag2)) {
//                            if (DmShared.xNull(voucherLink_Mdl1.getApplicableFrom()).equalsIgnoreCase(tag4)) {
                            if (DmShared.xNull(voucherLink_Mdl1.getSiteTag()).equalsIgnoreCase("")) {
                                throw new DmException(200, "Location,Overhead,site and applicable from combination already exist.");
                            } else if (DmShared.xNull(voucherLink_Mdl1.getSiteTag()).equalsIgnoreCase(DmShared.xNull(tag3))) {

                                throw new DmException(200, "Location,Overhead,site and applicable from combination already exist.");

                            }
//                            }
                        }
                    }
                }

                if (!isDataExists) {
                    isDataExists = true;
                }
            }
            if (!isDataExists) {
                throw new DmException(200, "Atleast one ITS should be define.");
            }
        }
    }
}
