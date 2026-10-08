package aarogya.dashBoard.walkThrough.server;

import aarogya.dashBoard.walkThrough.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WalkThrough_SrvImpl extends DmRSSPrivate implements WalkThrough_Srv {

    public DmResultPageing<List<GrdWalkThrough_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdWalkThrough_Mdl>> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<GrdWalkThrough_Mdl> listNode = new ArrayList<GrdWalkThrough_Mdl>();
        GrdWalkThrough_Mdl wThrough;
        dmrTemp.setData(listNode);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select vt.v_Type,max(vt.category) as category,max(vt.description) as description,count(wfp.linkDocId) as cnt "
                    + " from walkThroughFootPrint wfp "
                    + " left join voucher_Type vt on wfp.linkV_Type = vt.v_Type "
                    + " where isStatus = 0 and linkedUser like :userName "
                    + "	group by vt.v_Type ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("userName", "%" + DmShared.xNull(DmServer.getSsnUserName(this)) + ",%");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                wThrough = new GrdWalkThrough_Mdl();
                wThrough.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                wThrough.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                wThrough.setCategory(DmShared.xNull(rsTemp.getString("category")));
                wThrough.setPending(DmShared.xNull(rsTemp.getString("cnt")));
                listNode.add(wThrough);
            }

            helper.positioning(listNode.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());
            dmrTemp.setStatus(true);
            psTemp.close();
            rsTemp.close();
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

    public DmResult<CardDetail_Mdl> getCardDetail() {
        DmResult<CardDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get Chart Detail >====================================*/
            sqlQuery = "select count(wfp.linkDocId) as cnt "
                    + "from walkThroughFootPrint wfp "
                    + "where isStatus = 0 and linkedUser like :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", "%" + DmShared.xNull(DmServer.getSsnUserName(this)) + ",%");
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
