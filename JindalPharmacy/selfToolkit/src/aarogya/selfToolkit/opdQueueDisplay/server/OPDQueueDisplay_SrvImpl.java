package aarogya.selfToolkit.opdQueueDisplay.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.opdQueueDisplay.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.QueueAlgoType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OPDQueueDisplay_SrvImpl extends DmRSSPrivate implements OPDQueueDisplay_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String vrDate) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select  cm.subCode, sg.name "
                    + "		from consultantMast cm "
                    + "		left join subGroup sg on sg.subCode = cm.subCode "
                    + "		left join subGroupNature sgn on sgn.code = sg.filterNature "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		left join ("
                    + "			select  clm.consultantcode "
                    + "				from consultantLeaveMast clm "
                    + "				where :vrDate between clm.fromDate and clm.toDate "
                    + "				group by clm.consultantcode "
                    + "			) clm on clm.consultantcode = cm.subCode "
                    + "		where sgn.manualCode in ('CN') and sgsl.site_Code = :siteCode "
                    + "			and sg.isActive = 1 and clm.consultantcode is null ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setDate("vrDate", DmServer.convertDate(vrDate));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResult<List<GrdOPDQueue_Mdl>> getConsultationQueue(String consultant, String vrDate) {
        DmResult<List<GrdOPDQueue_Mdl>> dmrTemp = new DmResult();
        GrdOPDQueue_Mdl node;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdOPDQueue_Mdl> lstTemp = new ArrayList<>();
        dmrTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            sqlQuery = "select  qs.opdType, qs.currentTokenNo, qs.isCurrent, sg.name "
//                    + "		from queueSequence qs "
//                    + "		left join consultantOPDQueueDetail cqd on cqd.consultantCode = qs.consultantCode and qs.opdType = cqd.opdType "
//                    + "         left join subGroup sg on sg.subCode = qs.consultantCode "
//                    + "		where qs.consultantCode = :consultant and qs.v_date = :vrDate "
//                    + "             and qs.site_Code = :siteCode "
//                    + "		order by cqd.queuePriority ";
            sqlQuery = "select  cd.opdType, cd.tokenNo, qs.currentTokenNo, qs.isCurrent 		"
                    + "		from ("
                    + "			select  case when vt.category = 'OPD' then 2 "
                    + "					when vt.category = 'OPDEM' then 1  "
                    + "					when vt.category = 'OPDAP' then 3  "
                    + "					when vt.category = 'OPDRP' then 4  "
                    + "					when vt.category = 'OPDSK' then 5 "
                    + "                                 when vt.category = 'PAYCL' then 6 else 0 end opdType,max(cd.tokenNo) as tokenNo"
                    + "				from consultantDiary cd"
                    + "				left join voucher_Type vt on cd.v_Type = vt.v_Type"
                    + "				where cd.consultantCode = :consultant and cd.v_date = :vrDate and cd.uhid is not null             "
                    + "				and cd.site_Code = :siteCode and cd.nature = " + String.valueOf(STEnum.ConsultantDiaryNature.OPD.toInt()) + " 	"
                    + "				group by vt.category "
                    + "		) as cd"
                    + "		left join ("
                    + "			select  qs.opdType, qs.currentTokenNo, qs.isCurrent, cqd.queuePriority  		"
                    + "					from queueSequence qs 		"
                    + "					left join consultantOPDQueueDetail cqd on cqd.consultantCode = qs.consultantCode and qs.opdType = cqd.opdType          "
                    + "					where qs.consultantCode = :consultant and qs.v_date = :vrDate              "
                    + "					and qs.site_Code = :siteCode 	"
                    + "		) qs on cd.opdType = qs.opdType	"
                    + "		order by qs.queuePriority ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultant", DmShared.iNull(consultant).toString());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("vrDate", DmServer.convertDate(vrDate));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("opdType")).intValue() != 0) {
                    node = new GrdOPDQueue_Mdl();
                    node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("opdType")).toString());
                    node.setSharedKeyFieldName(QueueAlgoType.getKeyOf(DmShared.iNull(rsTemp.getString("opdType")).intValue()).getValue());
                    node.setTokenAssigned(DmShared.xNull(rsTemp.getString("tokenNo")));
                    node.setTokenRunning(DmShared.xNull(rsTemp.getString("currentTokenNo")));
                    node.setCurrent(DmShared.iNull(rsTemp.getString("isCurrent")).intValue() == 1);
                    lstTemp.add(node);
                }
            }
            rsTemp.close();
            psTemp.close();
            dmrTemp.setValue1(DmServer.getCodeViaManualCode(consultant, "subCode", "subGroup", "name", cnTemp));
            cnTemp.close();
            dmrTemp.setStatus(true);
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
