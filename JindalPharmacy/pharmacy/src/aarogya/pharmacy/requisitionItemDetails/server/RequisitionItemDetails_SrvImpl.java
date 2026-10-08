package aarogya.pharmacy.requisitionItemDetails.server;

import aarogya.pharmacy.requisitionItemDetails.shared.RequisitionItemDetails_Srv;
import dataman.dmBase.dmSession.server.DmRSSPrivate;

public class RequisitionItemDetails_SrvImpl extends DmRSSPrivate implements RequisitionItemDetails_Srv {

//    @Override
//    public DmResultPageing<List<GrdRequisitionDetails_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
//        DmResultPageing<List<GrdRequisitionDetails_Mdl>> rstTemp = new DmResultPageing<>();
//        List<GrdRequisitionDetails_Mdl> lstTemp = new ArrayList<>();
//        GrdRequisitionDetails_Mdl listNode;
//        Connection cnTemp = null;
//        ResultSet rsTemp = null;
//        DmPreparedStatement psTemp = null;
//        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
//        try {
//            rstTemp.setData(lstTemp);
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            sqlWithParam.setSQL("select  Max(mrq.docid) as docid,Max(mrq.recid) recid,Max(mrq.v_Date) as date, "
//                    + "Max(mrq.departmentCode) as departmentCode, Max(dm.name) as departmentName, "
//                    + "Max(mrq.requestedByCode) as requestedByCode, Max(rbc.name) as requestedByName  from medicineRequisition mrq "
//                    + "left Join (select docid from medicineRequisitionDetail where isStatus=0) as mrqd on mrqd.docId=	mrq.docid "
//                    + "left join voucher_Type vt on vt.v_Type=	mrq.v_Type "
//                    + "left join subGroup dm on dm.subCode=	mrq.departmentCode "
//                    + "left join subGroup rbc on rbc.subCode=	mrq.requestedByCode "
//                    + "where vt.category='PHREQ' and ISNULL(mrq.approvedBy,'')<>'' and mrq.site_Code='"+DmServer.getSsnSiteCode(this)+"' "
//                    + "Group by mrq.docid ");
//            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
//
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                listNode = new GrdRequisitionDetails_Mdl();
//                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
//                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
//                listNode.setDate(DmShared.dNull(rsTemp.getDate("date")));
//                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
//                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
//                listNode.setRequisitionByCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
//                listNode.setRequisitionByName(DmShared.xNull(rsTemp.getString("requestedByName")));
//                lstTemp.add(listNode);
//            }
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
//            rstTemp.setStatus(true);
//            rsTemp.close();
//            psTemp.close();
//            cnTemp.close();
//        } catch (Exception e) {
//            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return rstTemp;
//    }
}
