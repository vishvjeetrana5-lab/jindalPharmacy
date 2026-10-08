package aarogya.pharmacy.medicineTransfer.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class MedicineTransfer_Vld {

    public static void chkSave(MedicineTransfer_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getDepartmentFromTag(), "Department From", 200);
        DmShared.isValidText(mrTemp.getDepartmentToTag(), "Department To", 300);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Transfer Date", 400);

        if (!DmShared.dNull(mrTemp.getRefDate()).trim().equals("")) {
            if (DmShared.convertDate(DmShared.dNull(mrTemp.getRefDate())).getTime() > DmShared.convertDate(DmShared.dNull(mrTemp.getVoucherDate())).getTime()) {
                throw new DmException(500, "Ref. date can not be greater than transfer date.");
            }
        }

        if (DmShared.iNull(mrTemp.getDepartmentFromTag()).longValue() == DmShared.iNull(mrTemp.getDepartmentToTag()).longValue()) {
            throw new DmException(300, "Department From and Department To can not be same.");
        }

        boolean flag = false;
        String lineitemCodesCsv = "", incorrectBatchSql = "", incorrectBarcodeSql = "";;
        String requisitionItemChangeCondition = "";
        List<GrdMedicineDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdMedicineDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                    throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.iNull(detail_Mdl.getIbCode()).longValue() == 0) {
                    throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Batch No. is mandatory in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (!lineitemCodesCsv.trim().equalsIgnoreCase("")) {
                    lineitemCodesCsv += ",";
                }
                lineitemCodesCsv += DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());

                /*==============================<< Create condition string for Requisition item change check >>===============================*/
                if (!requisitionItemChangeCondition.trim().equalsIgnoreCase("")) {
                    requisitionItemChangeCondition += " OR ";
                } else {
                    requisitionItemChangeCondition += " WHERE ";
                }
                requisitionItemChangeCondition += " (rd.docId = '" + DmShared.xNull(detail_Mdl.getRequisitionDocId()) + "'";
                requisitionItemChangeCondition += " AND rd.v_SNo = '" + DmShared.xNull(detail_Mdl.getRequisitionV_SNo()) + "'";
                requisitionItemChangeCondition += " AND rd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

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
        mrTemp.setRequisitionItemChangeCondition(requisitionItemChangeCondition);
        mrTemp.setInCorrectSelectedBatchSql(incorrectBatchSql);
        mrTemp.setInCorrectSelectedBarcodeSql(incorrectBarcodeSql);

        if (!flag) {
            throw new DmException(600, "Medicines are mandatory.");
        }
    }

}
