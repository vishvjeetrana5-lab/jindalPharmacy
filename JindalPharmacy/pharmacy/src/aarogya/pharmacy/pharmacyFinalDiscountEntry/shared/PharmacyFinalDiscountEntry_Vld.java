package aarogya.pharmacy.pharmacyFinalDiscountEntry.shared;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PharmacyFinalDiscountEntry_Vld {

    public static void chkSave(PharmacyFinalDiscountEntry_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getCustomerTypeCode()), "Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getVoucherTypeCode()), "Voucher Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getRemark()), "Remark", 200);

        if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
            if (DmShared.iNull(mrTemp.getDefaultValue().getItemCode()).longValue() == 0) {
                throw new DmException(100, "Please set 'Pharmacy final Discount OH' in environment settings.");
            }
        }

        if (DmShared.iNull(mrTemp.getPlaceOfSupply()).longValue() != DmShared.iNull(mrTemp.getSiteStateCode()).longValue()) {
            throw new DmException(100, "Sale to other state is not allowed.");
        }

        boolean exists = false;
        double totalAmt = 0;
        if (mrTemp.getGrdDiscountDetails() != null) {
            for (int i = 0; i < mrTemp.getGrdDiscountDetails().size(); i++) {
                GrdDiscountDetails_Mdl mdl = mrTemp.getGrdDiscountDetails().get(i);
                if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    exists = true;
                    if (DmShared.iNull(mdl.getSaleAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl)).toString(), "Sales A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(mdl.getCgstAmt()).doubleValue() > 0 && DmShared.iNull(mdl.getCgstAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl)).toString(), "CGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(mdl.getSgstAmt()).doubleValue() > 0 && DmShared.iNull(mdl.getSgstAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl)).toString(), "SGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(mdl.getUgstAmt()).doubleValue() > 0 && DmShared.iNull(mdl.getUgstAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl)).toString(), "UGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl) + 1).toString() + ".");
                    }
                    if (DmShared.iNull(mdl.getSaleAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl)).toString(), "Sales A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdDiscountDetails().indexOf(mdl) + 1).toString() + ". Kindly set Sales A/c in environment settings.");
                    }
                    totalAmt += DmShared.vNull(mdl.getNetValue()).doubleValue();
                    if (!exists) {
                        exists = true;
                    }

                }
            }

            if (totalAmt != DmShared.vNull(mrTemp.getNetAmtToSave()).doubleValue()) {
                throw new DmException(400, "Please Adjust full discount Amt.");
            }

            if (!exists) {
                throw new DmException(300, "Discount Details Are Mandatory");
            }
        }

    }

}
