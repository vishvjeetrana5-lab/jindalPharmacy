package dataman.webJobs.staffInfo.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StaffInfo_Server {

    public static List<KeyFieldExtended> getStafInfo(String templateCode, Connection cnTemp) {
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended node;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            sqlQuery = "select  tsi.subCode, sg.name "
                    + "		from wjTemplateStaffInfo tsi "
                    + "		left join subGroup sg on sg.subCode = tsi.subCode "
                    + "		where tsi.templateCode = :code "
                    + "		order by sg.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", templateCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return lstTemp;
    }

    public static void saveStaffInfo(String templateCode, List<KeyFieldExtended> lstStaff, Connection cnTemp) {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            sqlQuery = "delete from wjTemplateStaffInfo where templateCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(templateCode).toString());
            psTemp.executeUpdate();
            psTemp.close();
            if (DmShared.iNull(templateCode).longValue() > 0 && lstStaff.size() > 0) {
                for (KeyFieldExtended staff : lstStaff) {
                    if (DmShared.iNull(staff.getSharedKeyFieldCode()).longValue() > 0) {
                        sqlQuery = "insert into wjTemplateStaffInfo (templateCode, subCode) values (:code, :subCode)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", templateCode);
                        psTemp.setString("subCode", DmShared.foriegnKey(staff.getSharedKeyFieldCode()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void deleteStaffInfo(String templateCode, Connection cnTemp) {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        try {
            sqlQuery = "delete from wjTemplateStaffInfo where templateCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(templateCode).toString());
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
