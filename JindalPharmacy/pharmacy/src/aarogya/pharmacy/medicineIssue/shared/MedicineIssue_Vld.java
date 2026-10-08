package aarogya.pharmacy.medicineIssue.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class MedicineIssue_Vld {

    public static void chkSave(MedicineIssue_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Issue Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Issue Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department In", 300);

        if (!DmShared.dNull(mrTemp.getRefDate()).equalsIgnoreCase("") && !DmShared.dNull(mrTemp.getVoucherDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getRefDate()).getTime() > DmShared.convertDate(mrTemp.getVoucherDate()).getTime()) {
                throw new DmException(400, "Ref Date can't be greater than Issue Date.");
            }
        }

        if (DmShared.iNull(mrTemp.getRequisitionDocId()).longValue() > 0 && DmShared.iNull(mrTemp.getIssueToTag()).intValue() == 1) {
            throw new DmException(600, "Expired Medicine can't be issued against requisition.");
        }

        boolean flag = false;
        String lineitemCodesCsv = "", incorrectBatchSql = "", incorrectBarcodeSql = "";
        String requisitionItemChangeCondition = "";
        List<GrdMedicineDetail_Mdl> listItemDetail = mrTemp.getListMedicineDetail();
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


                /*==============================<< Create condition string for Requisition item change check >>===============================*/
                if (!requisitionItemChangeCondition.trim().equalsIgnoreCase("")) {
                    requisitionItemChangeCondition += " OR ";
                } else {
                    requisitionItemChangeCondition += " WHERE ";
                }
                requisitionItemChangeCondition += " (rd.docId = '" + DmShared.xNull(detail_Mdl.getRequisitionDocId()) + "'";
                requisitionItemChangeCondition += " AND rd.v_SNo = '" + DmShared.xNull(detail_Mdl.getRequisitionV_SNo()) + "'";
                requisitionItemChangeCondition += " AND rd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                if (DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase("")) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Batch No. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

            }
        }

        mrTemp.setLineItemCodesCsv(lineitemCodesCsv);
        mrTemp.setRequisitionItemChangeCondition(requisitionItemChangeCondition);
        mrTemp.setInCorrectSelectedBatchSql(incorrectBatchSql);
        mrTemp.setInCorrectSelectedBarcodeSql(incorrectBarcodeSql);

        if (!flag) {
            throw new DmException(500, "Medicine details are mandatory.");
        }
    }

}
