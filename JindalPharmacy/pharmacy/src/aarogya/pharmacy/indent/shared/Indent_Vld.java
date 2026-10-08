package aarogya.pharmacy.indent.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class Indent_Vld {

    public static void chkSave(Indent_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getIndentTypeTag(), "Indent Type", 100);
        DmShared.isValidText(mrTemp.getDtIndent(), "Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department", 300);
        DmShared.isValidText(mrTemp.getIndentorTag(), "Indentor", 400);

        boolean flag = false;
        String lineitemCodesCsv = "";
        String reqItemChangeCondition = "";
        String reqQtyChangeCondition = "";

        List<GrdMedicineDetail_Mdl> listItemDetail = mrTemp.getGrdItemDetails();
        for (GrdMedicineDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                flag = true;

                /*==============================<< Create CSV for restricted department check >>===============================*/
                if (!lineitemCodesCsv.trim().equalsIgnoreCase("")) {
                    lineitemCodesCsv += ",";
                }
                lineitemCodesCsv += DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());

                if (!DmShared.xNull(detail_Mdl.getRequisitionDocId()).trim().equals("")) {

                    /*==============================<< Create condition string for REQUISITION item change check >>===============================*/
                    if (!reqItemChangeCondition.trim().equalsIgnoreCase("")) {
                        reqItemChangeCondition += " OR ";
                    } else {
                        reqItemChangeCondition = " WHERE ";
                    }
                    reqItemChangeCondition += " (rqd.docId = '" + DmShared.xNull(detail_Mdl.getRequisitionDocId()) + "'";
                    reqItemChangeCondition += " AND rqd.v_SNo = '" + DmShared.xNull(detail_Mdl.getRequisitionVsNo()) + "'";
                    reqItemChangeCondition += " AND rqd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

                    /*==============================<< Create condition string for REQUISITION Qty change check >>===============================*/
                    if (!reqQtyChangeCondition.trim().equalsIgnoreCase("")) {
                        reqQtyChangeCondition += " OR ";
                    } else {
                        reqQtyChangeCondition = " WHERE ";
                    }
                    reqQtyChangeCondition += " (rqd.docId = '" + DmShared.xNull(detail_Mdl.getRequisitionDocId()) + "'";
                    reqQtyChangeCondition += " AND rqd.v_SNo = '" + DmShared.xNull(detail_Mdl.getRequisitionVsNo()) + "'";
                    reqQtyChangeCondition += " AND (ISNULL(rqd.reqQty,0) - ISNULL(rqd.issueQty,0)) != " + DmShared.vNull(detail_Mdl.getQty().toString()) + ")";

                }

                /*==============================<< other validations >>===============================*/
                if ((!DmShared.dNull(mrTemp.getDtIndent()).equalsIgnoreCase("")) && (!DmShared.dNull(detail_Mdl.getReqDate()).equalsIgnoreCase(""))) {
                    if (DmShared.convertDate(detail_Mdl.getReqDate()).getTime() < DmShared.convertDate(mrTemp.getDtIndent()).getTime()) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Require Date can't be less than Indent Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
                if (!DmShared.dNull(detail_Mdl.getRequisitionDate()).equalsIgnoreCase("")) {
                    if (DmShared.convertDate(detail_Mdl.getRequisitionDate()).getTime() > DmShared.convertDate(mrTemp.getDtIndent()).getTime()) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Requisition Date can't be greater than Indent Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
            }
        }
        mrTemp.setLineItemCodesCsv(lineitemCodesCsv);
        mrTemp.setReqItemChangeCondition(reqItemChangeCondition);
        mrTemp.setReqQtyChangeCondition(reqQtyChangeCondition);

        if (!flag) {
            throw new DmException(500, "Indent detail is mandatory.");
        }

    }
}
