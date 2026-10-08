package aarogya.pharmacy.saleBillWholeSale.shared;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class SaleBillWholeSale_Vld {

    public static void chkSave(SaleBillWholeSale_Mdl mrTemp, String voucher_Cat) {
        boolean exists = false;
        String reqItemChangeCondition = "";
        DmShared.isValidText(DmShared.xNull(mrTemp.getVoucherTypeCode()).trim(), "Voucher Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getTypeCode()).trim(), "Debtor Type", 110);
        DmShared.isValidText(DmShared.xNull(mrTemp.getVoucherDate()).trim(), "Date", 200);
        DmShared.isValidText(DmShared.xNull(mrTemp.getDepartmentCode()).trim(), "Department", 300);
        DmShared.isValidText(DmShared.xNull(mrTemp.getPatientName()).trim(), "Customer Name", 400);
        DmShared.isValidText(mrTemp.getDueDate(), "'Due Date'", 410);
        DmShared.isValidText(DmShared.xNull(mrTemp.getPlaceOfSupplyName()).trim(), "State ", 500);
        DmShared.isValidText(DmShared.xNull(mrTemp.getLocationCode()).trim(), "GST ", 540);

        DmShared.isValidText(mrTemp.geteWayBillRequiredCode().trim(), "EWay Bill Required or Not ?", 560);

        if (DmShared.iNull(mrTemp.geteWayBillRequiredCode()).intValue() == 1) {
            DmShared.isValidText(mrTemp.getTransporterCode(), "Transporter", 550);
        }

        if (mrTemp.getGrdMediDetails() != null) {
            for (int i = 0; i < mrTemp.getGrdMediDetails().size(); i++) {
                GrdMedicine_Mdl medicineMdl = mrTemp.getGrdMediDetails().get(i);
                if (DmShared.iNull(mrTemp.getGrdMediDetails().get(i).getSharedKeyFieldCode()).longValue() > 0) {
                    if (DmShared.vNull(mrTemp.getGrdMediDetails().get(i).getFreeQuantityInPurchaseUnit()).doubleValue() == 0) {
                        if (DmShared.vNull(mrTemp.getGrdMediDetails().get(i).getQuantity()) == 0) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Please enter Medicine Quantity in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                    }
                    if (DmShared.vNull(mrTemp.getGrdMediDetails().get(i).getQuantity()) != 0) {
                        if (DmShared.vNull(medicineMdl.getAmount()) == 0) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Please enter Medicine Amount in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                    }
                    if (DmShared.vNull(medicineMdl.getIbCode()) == 0) {
                        throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Please enter Medicine Batch No. in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }
//                    if (DmShared.iNull(medicineMdl.getMedicineMdl().getRestrictGroup()).longValue() > 0) {
//                        if (DmShared.iNull(mrTemp.getConsultantCode()).longValue() == 0) {
//                            throw new DmException(740, "Consultant is mandatory.");
//                        }
//                    }

                    if (DmShared.vNull(medicineMdl.getAmount()) < 0 && !medicineMdl.isReturnSale()) {
                        throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Medicine Amount cannot be negative in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }

                    if (Math.abs(DmShared.vNull(medicineMdl.getAmount()).doubleValue()) > 0) {
                        if (Math.abs(DmShared.vNull(medicineMdl.getAmount()).doubleValue()) < Math.abs(DmShared.vNull(medicineMdl.getDiscount()).doubleValue())) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Discount given cannot be greater than amount : <b>" + medicineMdl.getSharedKeyFieldName() + "</b> in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                    }

                    if (voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL)) {
                        if (DmShared.iNull(medicineMdl.getSaleAcCode()).longValue() == 0) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Sales A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                        if (DmShared.vNull(medicineMdl.getCgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getCgstAcCode()).longValue() == 0) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "CGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                        if (DmShared.vNull(medicineMdl.getSgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getSgstAcCode()).longValue() == 0) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "SGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                        if (DmShared.vNull(medicineMdl.getUgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getUgstAcCode()).longValue() == 0) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "UGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                        if (DmShared.vNull(medicineMdl.getIgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getIgstAcCode()).longValue() == 0) {
                            throw new DmException(600, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "IGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                    }

                    if (!exists) {
                        exists = true;
                    }

                }
            }
        }

        if (voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL)) {
            if (DmShared.vNull(mrTemp.getRoundOffAmt()).doubleValue() != 0 && DmShared.iNull(mrTemp.getRoundOffAcCode()).longValue() == 0) {
                throw new DmException(700, "Round off A/c not found.");
            }
        }
        if (!exists) {
            throw new DmException(600, "GridRow", "0", "Medicine is mandatory.");
        }

    }
}
