package aarogya.dashBoard.medicineStockOut.server;

import aarogya.dashBoard.medicineStockOut.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

public class MedicineStockOut_SrvImpl extends DmRSSPrivate implements MedicineStockOut_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount) {
        DmResult<CardDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);
        String sql = "", departmentCode = "", departmentName = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            sqlQuery = "select subcode,name from subgroup where name in ('Jindal Pharmacy Unit-1', 'Jindal Pharmacy Unit-2'\n"
//                    + ",'Jindal Pharmacy Unit-3', 'Jindal Pharmacy Unit-4')";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//
//                if (DmShared.iNull(departmentCode).longValue() > 0) {
//                    departmentCode += ",";
//                    departmentName += ",";
//                }
//                departmentCode += DmShared.iNull(rsTemp.getString("subcode")).toString();
//                departmentName += DmShared.xNull(rsTemp.getString("name"));
//            }
//            rsTemp.close();
//            psTemp.close();

            mrTemp.setDepartmentCode(departmentCode);
            mrTemp.setDepartmentName(departmentName);

            sqlQuery = "  select count(tmp.itemName) as cnt from (\n"
                    + "\n"
                    + "select im.name as itemName,im.code as itemCode,im.purchaseUnitConversionRate,im.purchaseRate,um.code as skuCode,um.unit as skuName,dstk.currStkDept,sStk.currStkSite,dCnp.cnpDept,sCnp.cnpSite \n"
                    + "         ,(dstk.currStkDept-((dCnp.cnpDept/30))*40) as exc_Srt_Dept \n"
                    + "         ,(sstk.currStkSite-((sCnp.cnpSite/30))*40) as exc_Srt_Site \n"
                    + "		,case when dCnp.cnpDept>0 then dstk.currStkDept/((dCnp.cnpDept/30)) else 0 end as stkRunDaysDept \n"
                    + "		,case when sCnp.cnpSite>0 then sstk.currStkSite/((sCnp.cnpSite/30)) else 0 end as stkRunDaysSite \n"
                    + "		,isnull(drp.deptReqQty,0) as deptReqQty,isNull(srp.siteReqQty,0) as siteReqQty \n"
                    + "		,isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0) as pendingPoIndent \n"
                    + "		,(((dCnp.cnpDept/30))*40)-dstk.currStkDept-(isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0)) as recommendedQtyDept \n"
                    + "		,(((sCnp.cnpSite/30))*40)-sstk.currStkSite-(isNUll(spi.sitePendingIndent,0)+isNull(pord.PendingPO,0)) as recommendedQtySite \n"
                    + "         	from itemMast im \n"
                    + "                 /*++++++++++++++++++++++++ Department wise stock +++++++++++++++++++++++*/ \n"
                    + "                 left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(balanceQty) as currStkDept \n"
                    + "                             from itemBatchStock trn "
                    + "                             left join subgroup sg on sg.subcode=trn.departmentCode                           \n"
                    + "                             where trn.site_Code= :siteCode \n"
//                    + "                             AND  sg.name IN ('Pharmacy Store Retail', 'Pharmacy Store Wholesale' )             \n"
                    + "							 group by trn.itemCode  \n"
                    + "                         ) dStk on im.code=dStk.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Site wise stock +++++++++++++++++++++++*/                 left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(balanceQty) as currStkSite \n"
                    + "                             from itemBatchStock trn \n"
                    + "                             where trn.site_Code= :siteCode group by trn.itemCode \n"
                    + "                         ) sstk on im.code=sStk.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Department wise consumption +++++++++++++++++++++++*/                  left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(trn.isuQty) as cnpDept \n"
                    + "                             from stock trn \n"
                    + "                      left join subgroup sg on sg.subcode=trn.departmentCode                               "
                    + "                             left join voucher_Type vt on trn.v_Type=vt.v_Type \n"
                    + "                             where trn.site_Code= :siteCode  \n"
//                    + "                             AND  sg.name IN ('Pharmacy Store Retail', 'Pharmacy Store Wholesale' )                            \n"
                    + "							 and trn.v_Date between DATEADD(d,-30,cast(getdate() as date)) and cast(getdate() as date) \n"
                    + "                             and vt.category not in('MTRWS','MEDTR','ITMTR')  \n"
                    + "                             group by trn.itemCode \n"
                    + "                         ) dCnp on im.code=dCnp.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Site wise consumption +++++++++++++++++++++++*/                  left join   \n"
                    + "                     	( \n"
                    + "                         select trn.itemCode,sum(trn.isuQty) as cnpSite \n"
                    + "                             from stock trn \n"
                    + "                             left join voucher_Type vt on trn.v_Type=vt.v_Type \n"
                    + "                             where trn.site_Code= :siteCode  \n"
                    + "                             and trn.v_Date between DATEADD(d,-30,cast(getdate() as date)) and cast(getdate() as date) \n"
                    + "                             and vt.category not in('MTRWS','MEDTR','ITMTR')  \n"
                    + "                             group by trn.itemCode \n"
                    + "                         ) sCnp on im.code=sCnp.itemCode \n"
                    + "                 /*++++++++++++++++++++++++ Department wise pending requisition +++++++++++++++++++++++*/                  left join   \n"
                    + "                     	( \n"
                    + "                         select rqd.itemCode,sum(isnull(rqd.reqQty,0)-isnull(rqd.issueQty,0)-isnull(rqd.indentQty,0)) as deptReqQty \n"
                    + "                             from requisition trn \n"
                    + "                  left join subgroup sg on sg.subcode=trn.departmentCode     "
                    + "                             left join requisitionDetail rqd on trn.docid=rqd.docid \n"
                    + "                             where trn.site_Code =:siteCode and isnull(trn.approvedBy,'') <> '' and rqd.isStatus=0 \n"
//                    + "                             AND  sg.name IN ('Pharmacy Store Retail', 'Pharmacy Store Wholesale' )                        \n"
                    + "							 group by rqd.itemcode \n"
                    + "                         ) As dRp on dRp.ItemCode=im.Code \n"
                    + "                 /*++++++++++++++++++++++++ Site wise pending requisition +++++++++++++++++++++++*/                 left join   \n"
                    + "                     	( \n"
                    + "                         select rqd.itemCode,sum(isnull(rqd.reqQty,0)-isnull(rqd.issueQty,0)-isnull(rqd.indentQty,0)) as siteReqQty \n"
                    + "                             from requisition rs \n"
                    + "                             left join requisitionDetail rqd on rs.docid=rqd.docid \n"
                    + "                             where rs.site_Code = :siteCode and isnull(rs.approvedBy,'') <> '' and rqd.isStatus=0 \n"
                    + "                             group by rqd.itemcode \n"
                    + "                         ) As sRp on sRp.ItemCode=im.Code \n"
                    + "                 /*++++++++++++++++++++++++ Pending indent +++++++++++++++++++++++*/                 left join   \n"
                    + "                     	( \n"
                    + "                         SELECT rqd.itemCode, sum(isnull(rqd.aprQty,0)-isNUll(rqd.orderedQty,0)) as sitePendingIndent \n"
                    + "                             FROM indent as rq \n"
                    + "                             left join indentDetail rqd on rq.docid=rqd.docid \n"
                    + "                             WHERE  rq.site_Code=:siteCode  \n"
                    + "                             and rqd.isStatus=0  \n"
                    + "                             group by rqd.itemCode \n"
                    + "                         ) as spi on spi.itemCode=im.code \n"
                    + "                 /*++++++++++++++++++++++++ Pending pOrder +++++++++++++++++++++++*/                 left join   \n"
                    + "                     	( \n"
                    + "                         SELECT rqd.itemCode, sum(isnull(rqd.quantity,0)-isNUll(rqd.challanedQty,0)) as PendingPO \n"
                    + "                             FROM pOrder as rq \n"
                    + "                             left join pOrderDetail rqd on rq.docid=rqd.docid \n"
                    + "                             WHERE  rq.site_Code=:siteCode  \n"
                    + "                             and rqd.isStatus=0  \n"
                    + "                             group by rqd.itemCode \n"
                    + "                         ) as pord on pord.itemCode=im.code \n"
                    + "                 left join unitMast um on im.sku=um.code  \n"
                    + " where im.filterItemType=11 \n"
                    + ") as tmp\n"
                    + "\n"
                    + "where tmp.currStkDept=0 and tmp.cnpDept<>0  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCount(DmShared.iNull(rsTemp.getString("cnt")).toString());

                node = new DmChart_Mdl();
                node.setLabelName(DmShared.xNull(rsTemp.getString("cnt")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("cnt")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("cnt")).intValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListChardData().addAll(listNode);

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
