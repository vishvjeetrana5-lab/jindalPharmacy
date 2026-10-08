package dataman.dmToolkit.walkThrough.server;

import dataman.dmToolkit.walkThrough.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WalkThrough_SrvImpl extends DmRSSPrivate implements WalkThrough_Srv {

    @Override
    public DmResult<List<WalkThrough_Mdl>> getWalkThroughDetail(String wtaCode) {
        DmResult<List<WalkThrough_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<WalkThrough_Mdl> listNode = new ArrayList<WalkThrough_Mdl>();
        WalkThrough_Mdl node;
        dmrTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	wtaCode, v_SNo, authority, wtfTimeStamp, u_Name, remark, isStatus, linkedUser, "
                    + "		format(u_EntDt, 'dd-MMM-yyyy HH.mm') as checkedDT "
                    + "		from walkThroughFootPrint  "
                    + "		where wtaCode = :wtaCode "
                    + "		order by wtfTimeStamp desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("wtaCode", DmShared.iNull(wtaCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new WalkThrough_Mdl();
                node.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                node.setWtaV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setAuthority(DmShared.xNull(rsTemp.getString("authority")));
                node.setAuthorisedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                node.setAuthorisedDateTime(DmShared.xNull(rsTemp.getString("checkedDT")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setLinkedUser(DmShared.xNull(rsTemp.getString("linkedUser")));
                node.setStatus(DmServer.vNull(rsTemp.getString("isStatus"), "0"));
                node.setWtfTimeStamp(DmServer.vNull(rsTemp.getString("wtfTimeStamp"), "0"));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
