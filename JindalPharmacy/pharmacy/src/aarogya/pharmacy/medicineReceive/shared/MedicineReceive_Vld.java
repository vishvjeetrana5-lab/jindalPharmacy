package aarogya.pharmacy.medicineReceive.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class MedicineReceive_Vld {

    public static void chkSave(MedicineReceive_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department In", 300);
        if (!DmShared.dNull(mrTemp.getRefDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getRefDate()).getTime() > DmShared.convertDate(mrTemp.getVoucherDate()).getTime()) {
                throw new DmException(400, "Ref. Date cannot be greater than Receive Date.");
            }
        }

        boolean flag = false;
        String lineitemCodesCsv = "", incorrectBatchSql = "", incorrectBarcodeSql = "";
        List<GrdMedicineDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdMedicineDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                /*==============================<< Create CSV for restricted department check >>===============================*/
                if (!lineitemCodesCsv.trim().equalsIgnoreCase("")) {
                    lineitemCodesCsv += ",";
                }
                lineitemCodesCsv += DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());

                /*==============================<< Create Item Batch SQL For Invalid ibCode Validation >>===============================*/
                if (!incorrectBatchSql.equalsIgnoreCase("")) {
                    incorrectBatchSql += " UNION ALL ";
                }

                incorrectBatchSql += " SELECT " + detail_Mdl.getIbCode() + " as ibCode "
                        + " , " + detail_Mdl.getSharedKeyFieldCode() + " as itemCode "
                        + " , '" + detail_Mdl.getSharedKeyFieldName() + "' as itemName "
                        + " , '" + detail_Mdl.getBatchNo() + "' as batchNo ";


                /*==============================<< Create Item Batch SQL For Invalid/Barcode Validation >>===============================*/
                if (!DmShared.xNull(detail_Mdl.getBarCode()).trim().equals("")) {
                    if (!incorrectBarcodeSql.equalsIgnoreCase("")) {
                        incorrectBarcodeSql += " UNION ALL ";
                    }

                    incorrectBarcodeSql += " SELECT " + detail_Mdl.getIbCode() + " as ibCode "
                            + " , " + detail_Mdl.getSharedKeyFieldCode() + " as itemCode "
                            + " , '" + detail_Mdl.getSharedKeyFieldName() + "' as itemName "
                            + " , '" + detail_Mdl.getBatchNo() + "' as batchNo "
                            + " , '" + detail_Mdl.getBarCode() + "' as barcode ";
                }

            }
        }

        mrTemp.setLineItemCodesCsv(lineitemCodesCsv);
        mrTemp.setInCorrectSelectedBatchSql(incorrectBatchSql);
        mrTemp.setInCorrectSelectedBarcodeSql(incorrectBarcodeSql);

        if (!flag) {
            throw new DmException(500, "Medicines are mandatory.");
        }
    }

}
