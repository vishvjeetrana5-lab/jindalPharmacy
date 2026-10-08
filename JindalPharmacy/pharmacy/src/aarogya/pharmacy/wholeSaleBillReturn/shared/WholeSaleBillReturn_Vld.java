package aarogya.pharmacy.wholeSaleBillReturn.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class WholeSaleBillReturn_Vld {

    public static void chkSave(WholeSaleBillReturn_Mdl mrTemp) {
        boolean exists = false;
        String sbItemChangeCondition = "", incorrectBatchSql = "", incorrectBarcodeSql = "";
        DmShared.isValidText(DmShared.xNull(mrTemp.getVoucherTypeCode()).trim(), "Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getVoucherDate()).trim(), "Date", 200);
        DmShared.isValidText(DmShared.xNull(mrTemp.getDepartmentCode()).trim(), "Department", 300);
        DmShared.isValidText(DmShared.xNull(mrTemp.getDebtorName()).trim(), "Patient Name", 400);
        DmShared.isValidText(DmShared.xNull(mrTemp.getGstReasonCode()).trim(), "Reason", 410);
        DmShared.isValidText(DmShared.xNull(mrTemp.getLocationCode()).trim(), "GST Transaction", 420);

        if (!DmShared.dNull(mrTemp.getRefDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getRefDate()).getTime() > DmShared.convertDate(mrTemp.getVoucherDate()).getTime()) {
                throw new DmException(200, "Ref. Date can't be greater than return date.");
            }
        }

        if (mrTemp.getGrdMediDetails() != null) {
            for (int i = 0; i < mrTemp.getGrdMediDetails().size(); i++) {
                GrdMedicine_Mdl medicineMdl = mrTemp.getGrdMediDetails().get(i);
                if (DmShared.iNull(mrTemp.getGrdMediDetails().get(i).getSharedKeyFieldCode()).longValue() > 0) {
                    if (DmShared.vNull(mrTemp.getGrdMediDetails().get(i).getFreeQty()).doubleValue() == 0) {
                        if (DmShared.vNull(mrTemp.getGrdMediDetails().get(i).getQuantity()) == 0) {
                            throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Please enter Medicine Quantity in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                        if (DmShared.vNull(medicineMdl.getAmount()) == 0) {
                            throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Please enter Medicine Amount in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                        }
                    }
                    if (DmShared.vNull(medicineMdl.getIbCode()) == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Please enter Medicine Batch No. in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }
                    if (DmShared.iNull(medicineMdl.getSaleAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "Sales A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(medicineMdl.getCgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getCgstAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "CGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(medicineMdl.getSgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getSgstAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "SGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(medicineMdl.getUgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getUgstAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "UGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(medicineMdl.getIgstAmt()).doubleValue() > 0 && DmShared.iNull(medicineMdl.getIgstAcCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl)).toString(), "IGST A/c not found in line no. " + DmShared.iNull(mrTemp.getGrdMediDetails().indexOf(medicineMdl) + 1).toString() + ".");
                    }
                    if (!exists) {
                        exists = true;
                    }

                    if (!incorrectBatchSql.equalsIgnoreCase("")) {
                        incorrectBatchSql += " UNION ALL ";
                    }
                    incorrectBatchSql += " SELECT " + medicineMdl.getIbCode() + " as ibCode "
                            + " , " + medicineMdl.getSharedKeyFieldCode() + " as itemCode "
                            + " , '" + medicineMdl.getSharedKeyFieldName() + "' as itemName "
                            + " , '" + medicineMdl.getBatchNo() + "' as batchNo ";

                    /*==============================<< Create Item Batch SQL For Invalid Barcode Validation >>===============================*/
                    if (!DmShared.xNull(medicineMdl.getBarcode()).trim().equals("")) {
                        if (!incorrectBarcodeSql.equalsIgnoreCase("")) {
                            incorrectBarcodeSql += " UNION ALL ";
                        }

                        incorrectBarcodeSql += " SELECT " + medicineMdl.getIbCode() + " as ibCode "
                                + " , " + medicineMdl.getSharedKeyFieldCode() + " as itemCode "
                                + " , '" + medicineMdl.getSharedKeyFieldName() + "' as itemName "
                                + " , '" + medicineMdl.getBatchNo() + "' as batchNo "
                                + " , '" + medicineMdl.getBarcode() + "' as barcode ";
                    }

                    if (DmShared.iNull(medicineMdl.getSbDocId()).longValue() > 0) {

                        /*==============================<< Create condition string for REQUISITION item change check >>===============================*/
                        if (!sbItemChangeCondition.trim().equalsIgnoreCase("")) {
                            sbItemChangeCondition += " OR ";
                        } else {
                            sbItemChangeCondition = " WHERE ";
                        }
                        sbItemChangeCondition += " (sbd.docId = '" + DmShared.xNull(medicineMdl.getSbDocId()) + "'";
                        sbItemChangeCondition += " AND sbd.v_SNo = '" + DmShared.xNull(medicineMdl.getSbV_Sno()) + "'";
                        sbItemChangeCondition += " AND sbd.itemCode != '" + DmShared.xNull(medicineMdl.getSharedKeyFieldCode()) + "')";

                    }

                }
                if (DmShared.vNull(mrTemp.getNetAmount()).doubleValue() < 0) {
                    throw new DmException(700, "Negative amt. not allow.");
                }
            }
        }

        mrTemp.setSbItemChangeCondition(sbItemChangeCondition);

        if (DmShared.vNull(mrTemp.getRoundOffAmt()).doubleValue() != 0 && DmShared.iNull(mrTemp.getRoundOffAcCode()).longValue() == 0) {
            throw new DmException(700, "Round off A/c not found.");
        }
        if (!exists) {
            throw new DmException(500, "GridRow", "0", "Medicine is mandatory.");
        }

        if (DmShared.vNull(mrTemp.getNetAmount()).doubleValue() < 0) {
            throw new DmException(800, "Negative sale is not allow.");
        }
    }

}
